function [freq, gains] = gain_corr_res(M, O, S1, S2, TS, L, H)
%genarate expected times and gain for correlated signals + memoryless
rg=10:10:400;
gains = zeros(length(rg), 1);
freq = zeros(length(rg), 1);

res = residuals(M, [S1, S2]);

j = 1;
for i = rg
    [a, gains(j)] = corr_res(M, O, S1, S2, TS, i, L, H);
    freq(j) = length(S1)/length(find(res(:,1) > i));
    j = j + 1;
end