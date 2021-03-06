package com.spongycastle.openpgp.operator;

import com.spongycastle.bcpg.ContainedPacket;
import com.spongycastle.bcpg.SymmetricKeyAlgorithmTags;
import com.spongycastle.openpgp.PGPEncryptedDataGenerator;
import com.spongycastle.openpgp.PGPException;

/**
 * An encryption method that can be applied to encrypt data in a {@link PGPEncryptedDataGenerator}.
 */
public abstract class PGPKeyEncryptionMethodGenerator
{
    /**
     * Generates a packet encoding the details of this encryption method.
     * 
     * @param encAlgorithm the {@link SymmetricKeyAlgorithmTags encryption algorithm} being used
     * @param sessionInfo session data generated by the encrypted data generator.
     * @return a packet encoding the provided information and the configuration of this instance.
     * @throws PGPException if an error occurs constructing the packet.
     */
    public abstract ContainedPacket generate(int encAlgorithm, byte[] sessionInfo)
        throws PGPException;
}
