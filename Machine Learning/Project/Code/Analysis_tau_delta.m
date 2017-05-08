%This file helps to determine tau and delta for delta for CUSUM
data = dlmread('172.16.11.229-cont-135.csv');
dataSize = size(data);
n = data(2:end,2:end);
s = 25; %% detected through cross-corelation
sl1 = data(:,s);
f_z = find(sl1==0);

sl = sl1(1:f_z(1)-2150);

predata = 2000;

Mdl  = arx(s25,[1]); % declare model

resi = residuals(Mdl,sl);

fa_t1 = length(find(resi>=100));
fa_t2 = length(find(resi>=20));
fa_t3 = length(find(resi>=10));

y_fa_t1 = zeros(length(resi),1);
y_fa_t2 = zeros(length(resi),1);
y_fa_t3 = zeros(length(resi),1);

for i=1:length(resi)
    y_fa_t1(i,1) = 100;
    y_fa_t2(i,1) = 20;
    y_fa_t3(i,1) = 10;
end

figure
plot(resi);
hold on
line(1:length(resi),y_fa_t1, 'color','r')
line(1:length(resi),y_fa_t2, 'color','r')
line(1:length(resi),y_fa_t3, 'color','r')

cusum=zeros(length(resi)+1,1);

for i=1:length(resi)
   cusum(i+1,1)=max(0,cusum(i,1)  + resi(i,1)-2*mean(resi));
end
figure
plot(cusum);


