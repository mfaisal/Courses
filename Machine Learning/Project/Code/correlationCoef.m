data = dlmread('172.16.11.229-cont-135.csv');
dataSize = size(data);
n = data(2:end,2:end);
TS = data(2:end,1);

[R,P] = corrcoef(n);
[f,g] = find(P==0 & R>.9999);
M = [f,g]
length(M)
figure
plot(TS,n(:,M(1,1)));
hold on
plot(TS,n(:,M(1,2)));



