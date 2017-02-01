package com.spongycastle.cert.selector.jcajce;

import java.io.IOException;
import java.security.cert.X509CertSelector;

import com.spongycastle.asn1.ASN1OctetString;
import com.spongycastle.asn1.x500.X500Name;
import com.spongycastle.cert.selector.X509CertificateHolderSelector;

public class JcaSelectorConverter
{
    public JcaSelectorConverter()
    {

    }

    public X509CertificateHolderSelector getCertificateHolderSelector(X509CertSelector certSelector)
    {
        try
        {
            if (certSelector.getSubjectKeyIdentifier() != null)
            {
                return new X509CertificateHolderSelector(X500Name.getInstance(certSelector.getIssuerAsBytes()), certSelector.getSerialNumber(), ASN1OctetString.getInstance(certSelector.getSubjectKeyIdentifier()).getOctets());
            }
            else
            {
                return new X509CertificateHolderSelector(X500Name.getInstance(certSelector.getIssuerAsBytes()), certSelector.getSerialNumber());
            }
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("unable to convert issuer: " + e.getMessage());
        }
    }
}
