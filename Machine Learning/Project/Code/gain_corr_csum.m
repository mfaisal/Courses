function [freq, gains] = gain_corr_csum(M, O, S1, S2, TS, D, L, H)
%genarate expected times and gain for correlated signals + CUSUM
rg=10:10:400;
gains = zeros(length(rg), 1);
freq = zeros(length(rg), 1);

j = 1;
for i = rg
    [c, numalarms] = csum(M, [S1, S2], D, i);
    [a, c, gains(j)] = corr_csum(M, O, S1, S2, TS, i, D, L, H);
    freq(j) = (24*3600)/numalarms;
    j = j + 1;
end