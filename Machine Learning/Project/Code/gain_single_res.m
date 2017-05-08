function [freq, gains] = gain_single_res(M, O, S, TS, L, H)
%genarate expected times and gain for single signal + memoryless
rg=10:10:400;
gains = zeros(length(rg), 1);
freq = zeros(length(rg), 1);

res = residuals(M, S);

j = 1;
for i = rg
    [a, gains(j)] = single_res(M, O, S, TS, i, L, H);
    freq(j) = (24*3600)/length(find(res > i));
    j = j + 1;
end