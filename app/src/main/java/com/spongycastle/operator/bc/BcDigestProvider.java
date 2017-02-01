package com.spongycastle.operator.bc;

import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.crypto.ExtendedDigest;
import com.spongycastle.operator.OperatorCreationException;

public interface BcDigestProvider
{
    ExtendedDigest get(AlgorithmIdentifier digestAlgorithmIdentifier)
        throws OperatorCreationException;
}
