function [E] = residuals(Model, S)
%this funciton generate the absolute values of residuals
E = abs(predict(Model, S) - S);
