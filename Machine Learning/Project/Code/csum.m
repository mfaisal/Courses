function [csums, numalarms] = csum(M, S, D, T)
%for given M - Model, S-signal, D- delta, and T-tau this function
%return the number of alarms or hits to the Tau.

E = residuals(M, S);

csums = zeros(length(E(:,1)) + 1, 1);
numalarms = 0;
for i = 1:length(E(:,1))
    csums(i + 1, 1) = max(0, csums(i) + E(i,1) - D);
    if csums(i + 1, 1) > T
        csums(i + 1, 1) = 0;
        numalarms = numalarms + 1;
    end
end
