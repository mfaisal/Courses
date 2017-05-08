function [attacks, csums, gain] = single_csum(M, O, S, TS, T, D, L, H)

%M --> Model
%O --> Order of the model
%S --> Targeted signal
%TS -> Time series
%D --> Delta, used in CUSUM for preventing CUSUM increasing consistently
%under normal condition
%T --> Tau, theshold
%L --> Lower index for attack
%H --> Higher index for attack

real = S(1:H + 200);
attacks = zeros(length(real), 1);
csums = zeros(length(real), 1);

attacks(1:O) = real(1:O);
V = real(1:O);

cusum = 0;

for i = 1:length(real) - O;
    j = i + O - 1;
    
    if i <= L && L < j        
        %disp('2')
        V = [real(i:L);attacks(L + 1:j)];        
    elseif i <= H && H < j        
        %disp('4')
        V = [attacks(i:H);real(H + 1:j)];        
    elseif L < i && j <= H
        %disp('3')
        V = attacks(i:j);
    elseif j <= L || i > H
        %disp('1 | 5')
        V = real(i:j);
    end
    
    if i >= 2
        cusum = max(0, cusum + abs(V(O, 1) - p) - D);        
    end
    
    p = forecast(M, V, 1);
    
    if j <= L || j > H
        attacks(j + 1,1) = p;
        if cusum > T
            cusum = 0;
        end
    else
        attacks(j + 1,1) = p - (T + D - cusum);
    end
    csums(i) = cusum;        
end

gain = abs(attacks(H + 1) - real(L + 1));

hold on
plot(TS(1:H + 200), attacks)
hold on
plot(TS(1:H + 200), real)