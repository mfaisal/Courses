function [attacks, csums, gain] = corr_csum(M, O, S1, S2, TS, T, D, L, H)

%M --> Model
%O --> Order of the model
%S1 --> Targeted signal
%S2 --> Correlated signal
%TS -> Time series
%D --> Delta, used in CUSUM for preventing CUSUM increasing consistently
%under normal condition
%T --> Tau, theshold
%L --> Lower index for attack
%H --> Higher index for attack


real = S1(1:H + 200);
correlated = S2(1:H + 200);
attacks = zeros(length(real), 1);
csums = zeros(length(real), 1);

attacks(1:O) = real(1:O);
V = real(1:O);

cusum = 0;

for i = 1:length(real) - O;
    j = i + O - 1;
    
    if i <= L && L < j        
        
        V = [real(i:L);attacks(L + 1:j)];        
    elseif i <= H && H < j        
        
        V = [attacks(i:H);real(H + 1:j)];        
    elseif L < i && j <= H
        
        V = attacks(i:j);
    elseif j <= L || i > H
        
        V = real(i:j);
    end
    
    if i >= 2
        cusum = max(0, cusum + abs(V(O, 1) - p(1)) - D);        
    end
    
    p = forecast(M, [V, correlated(i:j);V, correlated(i:j)], 1);
    
    if j <= L || j > H
        attacks(j + 1,1) = p(1);
        if cusum > T
            cusum = 0;
        end
    else
        attacks(j + 1,1) = p(1) - (T + D - cusum);
    end
    csums(i) = cusum;
end

gain = abs(attacks(H + 1) - real(L + 1));

hold on
plot(TS(1:H + 200), attacks)
hold on
plot(TS(1:H + 200), real)