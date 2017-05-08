function [freq, gains] = gain_single_csum(M, O, S, TS, D, L, H)
%genarate expected times and gain for single signal + CUSUM
rg=10:10:400;
gains = zeros(length(rg), 1);
freq = zeros(length(rg), 1);

j = 1;
for i = rg
    [c, numalarms] = csum(M, S, D, i);
    [a, c, gains(j)] = single_csum(M, O, S, TS, i, D, L, H);
    freq(j) = (24*3600)/numalarms;
    j = j + 1;
end