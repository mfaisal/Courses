function [attacks, gain] = corr_res(M, O, S1, S2, TS, T, L, H)
%M --> Model
%O --> Order of the model
%S1 --> Targeted signal
%S2 --> Correlated signal
%TS -> Time series
%T --> Tau, theshold
%L --> Lower index for attack
%H --> Higher index for attack
real = S1(1:H + 200);
correlated = S2(1:H + 200);
attacks = zeros(length(real), 1);



attacks(1:O) = real(1:O);
V = real(1:O);

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
    
    p = forecast(M, [V, correlated(i:j);V, correlated(i:j)], 1);
    if j <= L || j > H
        attacks(j + 1,1) = p(1);
    else
        attacks(j + 1,1) = p(1) - T;
    end     
end

gain = abs(attacks(H + 1) - real(L + 1));

hold on
plot(TS(1:H + 200), attacks)
hold on
plot(TS(1:H + 200), real)

