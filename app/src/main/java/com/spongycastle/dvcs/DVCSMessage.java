package com.spongycastle.dvcs;

import com.spongycastle.asn1.ASN1Encodable;
import com.spongycastle.asn1.ASN1ObjectIdentifier;
import com.spongycastle.asn1.cms.ContentInfo;

public abstract class DVCSMessage
{
    private final ContentInfo contentInfo;

    protected DVCSMessage(ContentInfo contentInfo)
    {
        this.contentInfo = contentInfo;
    }

    public ASN1ObjectIdentifier getContentType()
    {
        return contentInfo.getContentType();
    }

    public abstract ASN1Encodable getContent();
}
