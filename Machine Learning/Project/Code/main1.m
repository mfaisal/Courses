%This file generates figure 6 in the report

clear ALL;
data = dlmread('172.16.11.229-cont-135.csv');
dataSize = size(data);
n = data(2:end,2:end);
s25 = n(:,25); % targeted signal
s24 = n(:,24); % most correlated signal with signal s25
TS = data(2:end,1);

                                    
arx_x = arx(s25,[1]); % model for single signal

taus = [100];%[10 50 100];
gains = zeros(length(taus),1);
gaincs = zeros(length(taus),1);
q=1;
delta = 40; %%cusum only


figure
grid on
for i=taus
    [a,gains(q,1)]= single_res(arx_x,1,s25,TS,i,1000,1500);
    q=q+1;
end


q=1;
for i=taus
    [a,c,gaincs(q,1)]= single_csum(arx_x,1,s25,TS,i,delta,1000,1500);
    q=q+1;
end

na = [1 1;1 1];                    
nb = zeros(2,0);                   
nk = zeros(2,0);                   
                                    
arx_x = arx([s25,s24],[na,nb,nk]); % model for correlated signals

figure
grid on
for i=taus
    [a,gains(q,1)]= corr_res(arx_x,1,s25,s24,TS,i,1000,1500);
    q=q+1;
end

q=1;
for i=tau
    [a,c,gaincs(q,1)]= corr_csum(arx_x,1,s25,s24,TS,i,delta,1000,1500);
    q=q+1;
end



