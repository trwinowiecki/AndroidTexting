package com.spongycastle.cms;

import com.spongycastle.asn1.ASN1OctetString;
import com.spongycastle.asn1.DEROctetString;
import com.spongycastle.asn1.cms.KEKIdentifier;
import com.spongycastle.asn1.cms.KEKRecipientInfo;
import com.spongycastle.asn1.cms.RecipientInfo;
import com.spongycastle.operator.GenericKey;
import com.spongycastle.operator.OperatorException;
import com.spongycastle.operator.SymmetricKeyWrapper;

public abstract class KEKRecipientInfoGenerator
    implements RecipientInfoGenerator
{
    private final KEKIdentifier kekIdentifier;

    protected final SymmetricKeyWrapper wrapper;

    protected KEKRecipientInfoGenerator(KEKIdentifier kekIdentifier, SymmetricKeyWrapper wrapper)
    {
        this.kekIdentifier = kekIdentifier;
        this.wrapper = wrapper;
    }

    public final RecipientInfo generate(GenericKey contentEncryptionKey)
        throws CMSException
    {
        try
        {
            ASN1OctetString encryptedKey = new DEROctetString(wrapper.generateWrappedKey(contentEncryptionKey));

            return new RecipientInfo(new KEKRecipientInfo(kekIdentifier, wrapper.getAlgorithmIdentifier(), encryptedKey));
        }
        catch (OperatorException e)
        {
            throw new CMSException("exception wrapping content key: " + e.getMessage(), e);
        }
    }
}