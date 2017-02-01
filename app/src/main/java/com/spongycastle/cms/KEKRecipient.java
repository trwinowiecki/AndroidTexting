package com.spongycastle.cms;

import com.spongycastle.asn1.x509.AlgorithmIdentifier;

public interface KEKRecipient
    extends Recipient
{
    RecipientOperator getRecipientOperator(AlgorithmIdentifier keyEncAlg, AlgorithmIdentifier contentEncryptionAlgorithm, byte[] encryptedContentKey)
        throws CMSException;
}
