package com.spongycastle.cert;

import com.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import com.spongycastle.operator.ContentVerifierProvider;
import com.spongycastle.operator.OperatorCreationException;

public interface X509ContentVerifierProviderBuilder
{
    ContentVerifierProvider build(SubjectPublicKeyInfo validatingKeyInfo)
        throws OperatorCreationException;

    ContentVerifierProvider build(X509CertificateHolder validatingKeyInfo)
        throws OperatorCreationException;
}
