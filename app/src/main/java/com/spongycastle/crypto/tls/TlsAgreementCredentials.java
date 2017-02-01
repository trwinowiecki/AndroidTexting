package com.spongycastle.crypto.tls;

import java.io.IOException;

import com.spongycastle.crypto.params.AsymmetricKeyParameter;

public interface TlsAgreementCredentials
    extends TlsCredentials
{
    byte[] generateAgreement(AsymmetricKeyParameter peerPublicKey)
        throws IOException;
}
