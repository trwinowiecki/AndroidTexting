package com.spongycastle.pkcs;

import java.io.IOException;

import com.spongycastle.asn1.ASN1Encodable;
import com.spongycastle.asn1.ASN1EncodableVector;
import com.spongycastle.asn1.ASN1ObjectIdentifier;
import com.spongycastle.asn1.DEROctetString;
import com.spongycastle.asn1.DERSet;
import com.spongycastle.asn1.pkcs.Attribute;
import com.spongycastle.asn1.pkcs.CertBag;
import com.spongycastle.asn1.pkcs.PKCSObjectIdentifiers;
import com.spongycastle.asn1.pkcs.PrivateKeyInfo;
import com.spongycastle.asn1.pkcs.SafeBag;
import com.spongycastle.asn1.x509.Certificate;
import com.spongycastle.asn1.x509.CertificateList;
import com.spongycastle.cert.X509CRLHolder;
import com.spongycastle.cert.X509CertificateHolder;
import com.spongycastle.operator.OutputEncryptor;

public class PKCS12SafeBagBuilder
{
    private ASN1ObjectIdentifier bagType;
    private ASN1Encodable        bagValue;
    private ASN1EncodableVector  bagAttrs = new ASN1EncodableVector();

    public PKCS12SafeBagBuilder(PrivateKeyInfo privateKeyInfo, OutputEncryptor encryptor)
    {
        this.bagType = PKCSObjectIdentifiers.pkcs8ShroudedKeyBag;
        this.bagValue = new PKCS8EncryptedPrivateKeyInfoBuilder(privateKeyInfo).build(encryptor).toASN1Structure();
    }

    public PKCS12SafeBagBuilder(PrivateKeyInfo privateKeyInfo)
    {
        this.bagType = PKCSObjectIdentifiers.keyBag;
        this.bagValue = privateKeyInfo;
    }

    public PKCS12SafeBagBuilder(X509CertificateHolder certificate)
        throws IOException
    {
        this(certificate.toASN1Structure());
    }

    public PKCS12SafeBagBuilder(X509CRLHolder crl)
        throws IOException
    {
        this(crl.toASN1Structure());
    }

    public PKCS12SafeBagBuilder(Certificate certificate)
        throws IOException
    {
        this.bagType = PKCSObjectIdentifiers.certBag;
        this.bagValue = new CertBag(PKCSObjectIdentifiers.x509Certificate, new DEROctetString(certificate.getEncoded()));
    }

    public PKCS12SafeBagBuilder(CertificateList crl)
        throws IOException
    {
        this.bagType = PKCSObjectIdentifiers.crlBag;
        this.bagValue = new CertBag(PKCSObjectIdentifiers.x509Crl, new DEROctetString(crl.getEncoded()));
    }

    public PKCS12SafeBagBuilder addBagAttribute(ASN1ObjectIdentifier attrType, ASN1Encodable attrValue)
    {
        bagAttrs.add(new Attribute(attrType, new DERSet(attrValue)));

        return this;
    }

    public PKCS12SafeBag build()
    {
        return new PKCS12SafeBag(new SafeBag(bagType, bagValue, new DERSet(bagAttrs)));
    }
}
