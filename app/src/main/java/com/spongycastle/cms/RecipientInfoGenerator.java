package com.spongycastle.cms;

import com.spongycastle.asn1.cms.RecipientInfo;
import com.spongycastle.operator.GenericKey;

public interface RecipientInfoGenerator
{
    RecipientInfo generate(GenericKey contentEncryptionKey)
        throws CMSException;
}
