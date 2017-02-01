package com.spongycastle.cert.ocsp.jcajce;

import java.security.PublicKey;

import javax.security.auth.x500.X500Principal;

import com.spongycastle.asn1.x500.X500Name;
import com.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import com.spongycastle.cert.ocsp.OCSPException;
import com.spongycastle.cert.ocsp.RespID;
import com.spongycastle.operator.DigestCalculator;

public class JcaRespID
    extends RespID
{
    public JcaRespID(X500Principal name)
    {
        super(X500Name.getInstance(name.getEncoded()));
    }

    public JcaRespID(PublicKey pubKey, DigestCalculator digCalc)
        throws OCSPException
    {
        super(SubjectPublicKeyInfo.getInstance(pubKey.getEncoded()), digCalc);
    }
}
