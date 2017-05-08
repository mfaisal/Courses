function [attacks, gain] = single_res(M, O, S, TS, T, L, H)
%M --> Model
%O --> Order of the model
%S --> Targeted signal
%TS -> Time series
%T --> Tau, theshold
%L --> Lower index for attack
%H --> Higher index for attack

real = S(1:H + 200);
attacks = zeros(length(real), 1);



attacks(1:O) = real(1:O);
V = real(1:O);

for i = 1:length(real) - O; % genearting the attack
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
    
    p = forecast(M, V, 1);
    if j <= L || j > H
        attacks(j + 1,1) = p;
    else
        attacks(j + 1,1) = p - T;
    end     
end

gain = abs(attacks(H + 1) - real(L + 1));

hold on
plot(TS(1:H + 200), attacks)
hold on
plot(TS(1:H + 200), real)
