package com.spongycastle.cert.crmf;

import com.spongycastle.asn1.DERBitString;
import com.spongycastle.asn1.crmf.CertRequest;
import com.spongycastle.asn1.crmf.PKMACValue;
import com.spongycastle.asn1.crmf.POPOSigningKey;
import com.spongycastle.asn1.crmf.POPOSigningKeyInput;
import com.spongycastle.asn1.x509.GeneralName;
import com.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import com.spongycastle.operator.ContentSigner;

public class ProofOfPossessionSigningKeyBuilder
{
    private CertRequest certRequest;
    private SubjectPublicKeyInfo pubKeyInfo;
    private GeneralName name;
    private PKMACValue publicKeyMAC;

    public ProofOfPossessionSigningKeyBuilder(CertRequest certRequest)
    {
        this.certRequest = certRequest;
    }


    public ProofOfPossessionSigningKeyBuilder(SubjectPublicKeyInfo pubKeyInfo)
    {
        this.pubKeyInfo = pubKeyInfo;
    }

    public ProofOfPossessionSigningKeyBuilder setSender(GeneralName name)
    {
        this.name = name;

        return this;
    }

    public ProofOfPossessionSigningKeyBuilder setPublicKeyMac(PKMACValueGenerator generator, char[] password)
        throws CRMFException
    {
        this.publicKeyMAC = generator.generate(password, pubKeyInfo);

        return this;
    }

    public POPOSigningKey build(ContentSigner signer)
    {
        if (name != null && publicKeyMAC != null)
        {
            throw new IllegalStateException("name and publicKeyMAC cannot both be set.");
        }

        POPOSigningKeyInput popo;

        if (certRequest != null)
        {
            popo = null;

            CRMFUtil.derEncodeToStream(certRequest, signer.getOutputStream());
        }
        else if (name != null)
        {
            popo = new POPOSigningKeyInput(name, pubKeyInfo);

            CRMFUtil.derEncodeToStream(popo, signer.getOutputStream());
        }
        else
        {
            popo = new POPOSigningKeyInput(publicKeyMAC, pubKeyInfo);

            CRMFUtil.derEncodeToStream(popo, signer.getOutputStream());
        }

        return new POPOSigningKey(popo, signer.getAlgorithmIdentifier(), new DERBitString(signer.getSignature()));
    }
}
