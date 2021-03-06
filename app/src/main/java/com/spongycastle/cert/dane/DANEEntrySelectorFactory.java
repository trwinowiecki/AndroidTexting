package com.spongycastle.cert.dane;

import java.io.IOException;
import java.io.OutputStream;

import com.spongycastle.operator.DigestCalculator;
import com.spongycastle.util.Strings;
import com.spongycastle.util.encoders.Hex;

/**
 * Factory for creating selector objects to use with the DANECertificateStore.
 */
public class DANEEntrySelectorFactory
{
    private final DigestCalculator digestCalculator;

    /**
     * Base constructor.
     *
     * @param digestCalculator a calculator for the message digest to filter email addresses currently SHA-224.
     */
    public DANEEntrySelectorFactory(DigestCalculator digestCalculator)
    {
        this.digestCalculator = digestCalculator;
    }

    /**
     * Create a selector for the passed in email address.
     * @param emailAddress the emails address of interest.
     * @throws DANEException in case of issue generating a matching name.
     */
    public DANEEntrySelector createSelector(String emailAddress)
        throws DANEException
    {
        final byte[] enc = Strings.toUTF8ByteArray(emailAddress.substring(0, emailAddress.indexOf('@')));

        try
        {
            OutputStream cOut = digestCalculator.getOutputStream();

            cOut.write(enc);

            cOut.close();
        }
        catch (IOException e)
        {
            throw new DANEException("Unable to calculate digest string: " + e.getMessage(), e);
        }

        byte[] hash = digestCalculator.getDigest();

        final String domainName = Strings.fromByteArray(Hex.encode(hash)) + "._smimecert." + emailAddress.substring(emailAddress.indexOf('@') + 1);

        return new DANEEntrySelector(domainName);
    }
}
