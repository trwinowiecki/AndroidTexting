package com.spongycastle.openpgp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.spongycastle.bcpg.SignatureSubpacket;
import com.spongycastle.bcpg.SignatureSubpacketTags;
import com.spongycastle.bcpg.sig.EmbeddedSignature;
import com.spongycastle.bcpg.sig.Exportable;
import com.spongycastle.bcpg.sig.Features;
import com.spongycastle.bcpg.sig.IssuerKeyID;
import com.spongycastle.bcpg.sig.KeyExpirationTime;
import com.spongycastle.bcpg.sig.KeyFlags;
import com.spongycastle.bcpg.sig.NotationData;
import com.spongycastle.bcpg.sig.PreferredAlgorithms;
import com.spongycastle.bcpg.sig.PrimaryUserID;
import com.spongycastle.bcpg.sig.Revocable;
import com.spongycastle.bcpg.sig.RevocationKey;
import com.spongycastle.bcpg.sig.RevocationKeyTags;
import com.spongycastle.bcpg.sig.RevocationReason;
import com.spongycastle.bcpg.sig.SignatureCreationTime;
import com.spongycastle.bcpg.sig.SignatureExpirationTime;
import com.spongycastle.bcpg.sig.SignerUserID;
import com.spongycastle.bcpg.sig.TrustSignature;

/**
 * Generator for signature subpackets.
 */
public class PGPSignatureSubpacketGenerator
{
    List list = new ArrayList();

    public PGPSignatureSubpacketGenerator()
    {
    }

    public void setRevocable(boolean isCritical, boolean isRevocable)
    {
        list.add(new Revocable(isCritical, isRevocable));
    }

    public void setExportable(boolean isCritical, boolean isExportable)
    {
        list.add(new Exportable(isCritical, isExportable));
    }

    public void setFeature(boolean isCritical, byte feature)
    {
        list.add(new Features(isCritical, feature));
    }

    /**
     * Add a TrustSignature packet to the signature. The values for depth and trust are
     * largely installation dependent but there are some guidelines in RFC 4880 -
     * 5.2.3.13.
     * 
     * @param isCritical true if the packet is critical.
     * @param depth depth level.
     * @param trustAmount trust amount.
     */
    public void setTrust(boolean isCritical, int depth, int trustAmount)
    {
        list.add(new TrustSignature(isCritical, depth, trustAmount));
    }

    /**
     * Set the number of seconds a key is valid for after the time of its creation. A
     * value of zero means the key never expires.
     * 
     * @param isCritical true if should be treated as critical, false otherwise.
     * @param seconds
     */
    public void setKeyExpirationTime(boolean isCritical, long seconds)
    {
        list.add(new KeyExpirationTime(isCritical, seconds));
    }

    /**
     * Set the number of seconds a signature is valid for after the time of its creation.
     * A value of zero means the signature never expires.
     * 
     * @param isCritical true if should be treated as critical, false otherwise.
     * @param seconds
     */
    public void setSignatureExpirationTime(boolean isCritical, long seconds)
    {
        list.add(new SignatureExpirationTime(isCritical, seconds));
    }

    /**
     * Set the creation time for the signature.
     * <p>
     * Note: this overrides the generation of a creation time when the signature is
     * generated.
     */
    public void setSignatureCreationTime(boolean isCritical, Date date)
    {
        list.add(new SignatureCreationTime(isCritical, date));
    }

    public void setPreferredHashAlgorithms(boolean isCritical, int[] algorithms)
    {
        list.add(new PreferredAlgorithms(SignatureSubpacketTags.PREFERRED_HASH_ALGS, isCritical,
            algorithms));
    }

    public void setPreferredSymmetricAlgorithms(boolean isCritical, int[] algorithms)
    {
        list.add(new PreferredAlgorithms(SignatureSubpacketTags.PREFERRED_SYM_ALGS, isCritical,
            algorithms));
    }

    public void setPreferredCompressionAlgorithms(boolean isCritical, int[] algorithms)
    {
        list.add(new PreferredAlgorithms(SignatureSubpacketTags.PREFERRED_COMP_ALGS, isCritical,
            algorithms));
    }

    public void setKeyFlags(boolean isCritical, int flags)
    {
        list.add(new KeyFlags(isCritical, flags));
    }

    public void setSignerUserID(boolean isCritical, String userID)
    {
        if (userID == null)
        {
            throw new IllegalArgumentException("attempt to set null SignerUserID");
        }

        list.add(new SignerUserID(isCritical, userID));
    }

    public void setSignerUserID(boolean isCritical, byte[] rawUserID)
    {
        if (rawUserID == null)
        {
            throw new IllegalArgumentException("attempt to set null SignerUserID");
        }

        list.add(new SignerUserID(isCritical, false, rawUserID));
    }

    public void setEmbeddedSignature(boolean isCritical, PGPSignature pgpSignature)
        throws IOException
    {
        byte[] sig = pgpSignature.getEncoded();
        byte[] data;

        if (sig.length - 1 > 256)
        {
            data = new byte[sig.length - 3];
        }
        else
        {
            data = new byte[sig.length - 2];
        }

        System.arraycopy(sig, sig.length - data.length, data, 0, data.length);

        list.add(new EmbeddedSignature(isCritical, false, data));
    }

    public void setPrimaryUserID(boolean isCritical, boolean isPrimaryUserID)
    {
        list.add(new PrimaryUserID(isCritical, isPrimaryUserID));
    }

    public void setNotationData(boolean isCritical, boolean isHumanReadable, String notationName,
        String notationValue)
    {
        list.add(new NotationData(isCritical, isHumanReadable, notationName, notationValue));
    }

    /**
     * Sets revocation reason sub packet
     */
    public void setRevocationReason(boolean isCritical, byte reason, String description)
    {
        list.add(new RevocationReason(isCritical, reason, description));
    }

    /**
     * Sets revocation key sub packet
     */
    public void setRevocationKey(boolean isCritical, int keyAlgorithm, byte[] fingerprint)
    {
        list.add(new RevocationKey(isCritical, RevocationKeyTags.CLASS_DEFAULT, keyAlgorithm,
            fingerprint));
    }

    /**
     * Sets issuer key sub packe
     */
    public void setIssuerKeyID(boolean isCritical, long keyID)
    {
        list.add(new IssuerKeyID(isCritical, keyID));
    }

    public PGPSignatureSubpacketVector generate()
    {
        return new PGPSignatureSubpacketVector(
            (SignatureSubpacket[])list.toArray(new SignatureSubpacket[list.size()]));
    }
}
