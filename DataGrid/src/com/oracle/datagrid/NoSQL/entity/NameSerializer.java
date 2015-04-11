/*-
 *
 *  This file is part of Oracle NoSQL Database
 *  Copyright (C) 2011, 2014 Oracle and/or its affiliates.  All rights reserved.
 *
 *  Oracle NoSQL Database is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Affero General Public License
 *  as published by the Free Software Foundation, version 3.
 *
 *  Oracle NoSQL Database is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public
 *  License in the LICENSE file along with Oracle NoSQL Database.  If not,
 *  see <http://www.gnu.org/licenses/>.
 *
 *  An active Oracle commercial licensing agreement for this product
 *  supercedes this license.
 *
 *  For more information please contact:
 *
 *  Vice President Legal, Development
 *  Oracle America, Inc.
 *  5OP-10
 *  500 Oracle Parkway
 *  Redwood Shores, CA 94065
 *
 *  or
 *
 *  berkeleydb-info_us@oracle.com
 *
 *  [This line intentionally left blank.]
 *  [This line intentionally left blank.]
 *  [This line intentionally left blank.]
 *  [This line intentionally left blank.]
 *  [This line intentionally left blank.]
 *  [This line intentionally left blank.]
 *  EOF
 *
 */

package com.oracle.datagrid.NoSQL.entity;

import com.tangosol.io.pof.PofSerializer;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides POF serialization of Name.
 */
public class NameSerializer implements PofSerializer {
    final private static int VERSION_ID = 1;
    final private static int FIRSTNAME = 1;
    final private static int LASTNAME = 2;
    
    /**
     * Default constructor
     */
    public NameSerializer() {}

    /**
     * Serialize a object.
     *
     * @param out The PofWriter to write to
     * @param o The Name object to write
     * @throws IOException if the object o is not a Name
     */
    @Override
    public void serialize(PofWriter out, Object o)
        throws IOException
    {
        if (o.getClass() == Name.class) {
            out.setVersionId(VERSION_ID);
            out.writeString(FIRSTNAME, ((Name)o).getFirstName());
            out.writeString(LASTNAME, ((Name)o).getLastName());
            out.writeRemainder(null);
        } else {
            throw new IOException(
                "Invalid type presented to Name Serializer: " + o.getClass());
        }
    }

    /**
     * Deserialize a Name object.
     *
     * @param in The PofReader to read from
     * @return  The Name object read from the input stream
     * @throws IOException if the stream does not contain the serialization
     *         of a Name object in an expected version.
     */
    @Override
    public Object deserialize(PofReader in)
        throws IOException
    {
        if (in.getVersionId() != VERSION_ID) {
            throw new IOException(
                "NameSerializer encountered unexpected " +
                "version id: " + in.getVersionId());
        }

        final String firstName = in.readString(FIRSTNAME);
        final String lastName = in.readString(LASTNAME);
        in.readRemainder();

        final Name name = new Name();
        name.setFirstName(firstName);
        name.setLastName(lastName);
        return name;
    }
}