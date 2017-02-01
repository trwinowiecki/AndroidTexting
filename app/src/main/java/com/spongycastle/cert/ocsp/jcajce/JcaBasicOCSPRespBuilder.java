package com.spongycastle.cert.ocsp.jcajce;

import java.security.PublicKey;

import com.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import com.spongycastle.cert.ocsp.BasicOCSPRespBuilder;
import com.spongycastle.cert.ocsp.OCSPException;
import com.spongycastle.operator.DigestCalculator;

public class JcaBasicOCSPRespBuilder
    extends BasicOCSPRespBuilder
{
    public JcaBasicOCSPRespBuilder(PublicKey key, DigestCalculator digCalc)
        throws OCSPException
    {
        super(SubjectPublicKeyInfo.getInstance(key.getEncoded()), digCalc);
    }
}
