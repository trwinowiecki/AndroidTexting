package com.spongycastle.cms.bc;

import java.io.IOException;

import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.cert.X509CertificateHolder;
import com.spongycastle.crypto.params.AsymmetricKeyParameter;
import com.spongycastle.operator.bc.BcRSAAsymmetricKeyWrapper;

public class BcRSAKeyTransRecipientInfoGenerator
    extends BcKeyTransRecipientInfoGenerator
{
    public BcRSAKeyTransRecipientInfoGenerator(byte[] subjectKeyIdentifier, AlgorithmIdentifier encAlgId, AsymmetricKeyParameter publicKey)
    {
        super(subjectKeyIdentifier, new BcRSAAsymmetricKeyWrapper(encAlgId, publicKey));
    }

    public BcRSAKeyTransRecipientInfoGenerator(X509CertificateHolder recipientCert)
        throws IOException
    {
        super(recipientCert, new BcRSAAsymmetricKeyWrapper(recipientCert.getSubjectPublicKeyInfo().getAlgorithmId(), recipientCert.getSubjectPublicKeyInfo()));
    }
}
