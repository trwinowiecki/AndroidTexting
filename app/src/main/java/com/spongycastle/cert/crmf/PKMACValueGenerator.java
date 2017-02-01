package com.spongycastle.cert.crmf;

import java.io.IOException;
import java.io.OutputStream;

import com.spongycastle.asn1.ASN1Encoding;
import com.spongycastle.asn1.DERBitString;
import com.spongycastle.asn1.crmf.PKMACValue;
import com.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import com.spongycastle.operator.MacCalculator;

class PKMACValueGenerator
{
    private PKMACBuilder builder;

    public PKMACValueGenerator(PKMACBuilder builder)
    {
        this.builder = builder;
    }

    public PKMACValue generate(char[] password, SubjectPublicKeyInfo keyInfo)
        throws CRMFException
    {
        MacCalculator calculator = builder.build(password);

        OutputStream macOut = calculator.getOutputStream();

        try
        {
            macOut.write(keyInfo.getEncoded(ASN1Encoding.DER));

            macOut.close();
        }
        catch (IOException e)
        {
            throw new CRMFException("exception encoding mac input: " + e.getMessage(), e);
        }

        return new PKMACValue(calculator.getAlgorithmIdentifier(), new DERBitString(calculator.getMac()));
    }
}
