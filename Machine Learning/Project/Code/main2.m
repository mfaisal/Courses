%This file generate the figure 7 in project report
clear ALL;
data = dlmread('172.16.11.229-cont-135.csv');
dataSize = size(data);
n = data(2:end,2:end);
s25 = n(:,25);
s24 = n(:,24);
TS = data(2:end,1);
delta = 40; %%cusum only
                
                                    
 arx1 = arx(s25,[1]);

figure
[freq_s_r, gains_s_r] = gain_single_res(arx1, 1, s25, TS, 1000, 1500); % geneate expected time for false alram and corresponding gain with single signal
figure % using memoryless method 
[freq_s_c, gains_s_c] = gain_single_csum(arx1, 1, s25, TS, delta, 1000, 1500); % single signal + CUSUM


na = [1 1;1 1];                    
nb = zeros(2,0);                   
nk = zeros(2,0);                   
                                    
arx_x = arx([s25,s24],[na,nb,nk]); % model for correlated signals

figure
[freq_c_r, gains_c_r] = gain_corr_res(arx_x, 1, s25, s24, TS, 1000, 1500); % correlated signals + memoryless
figure
[freq_c_c, gains_c_c] = gain_corr_csum(arx_x, 1, s25, s24, TS, delta, 1000, 1500); % correlated signals + CUSUM

figure
plot(freq_s_r,gains_s_r);
hold on
plot(freq_c_r, gains_c_r);
figure
plot(freq_s_c,gains_s_c);
hold on
plot(freq_c_c, gains_c_c);
grid on;
