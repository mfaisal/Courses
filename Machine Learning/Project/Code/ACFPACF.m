% Mutafa Faisal
% maf120030
% This file calculate the ACF and PACF for signal 25 (index in time series excluding time series)

data = dlmread('172.16.11.229-cont-135.csv');
dataSize = size(data);
n = data(2:end,2:end);
s = 25; %% detected through cross-corelation
s25 = n(:,s);

figure
subplot(2,1,1)
autocorr(s25) % auto correlation for signal 25
subplot(2,1,2)
parcorr(s25) % partial auto correlation for signal 25

y=diff(s25); % diff for singnal 25
figure 
plot(y)

figure
subplot(2,1,1)
autocorr(y) % auto correlation for diff signal
subplot(2,1,2)
parcorr(y) % partial auto correlation for diff signal
