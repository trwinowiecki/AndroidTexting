package com.spongycastle.pkcs;

import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.operator.MacCalculator;
import com.spongycastle.operator.OperatorCreationException;

public interface PKCS12MacCalculatorBuilder
{
    MacCalculator build(char[] password)
        throws OperatorCreationException;

    AlgorithmIdentifier getDigestAlgorithmIdentifier();
}
