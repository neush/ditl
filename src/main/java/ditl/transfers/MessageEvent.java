/*******************************************************************************
 * This file is part of DITL.                                                  *
 *                                                                             *
 * Copyright (C) 2011-2012 John Whitbeck <john@whitbeck.fr>                    *
 *                                                                             *
 * DITL is free software: you can redistribute it and/or modify                *
 * it under the terms of the GNU General Public License as published by        *
 * the Free Software Foundation, either version 3 of the License, or           *
 * (at your option) any later version.                                         *
 *                                                                             *
 * DITL is distributed in the hope that it will be useful,                     *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of              *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the               *
 * GNU General Public License for more details.                                *
 *                                                                             *
 * You should have received a copy of the GNU General Public License           *
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.       *
 *******************************************************************************/
package ditl.transfers;

import java.io.IOException;

import ditl.CodedBuffer;
import ditl.CodedInputStream;
import ditl.Item;

public class MessageEvent implements Item {

    public enum Type {
        NEW, EXPIRE
    }

    Integer msg_id;
    Type _type;

    public MessageEvent(Integer msgId, Type type) {
        msg_id = msgId;
        _type = type;
    }

    public Integer msgId() {
        return msg_id;
    }

    public boolean isNew() {
        return _type == Type.NEW;
    }

    public Message message() {
        return new Message(msg_id);
    }

    public static final class Factory implements Item.Factory<MessageEvent> {
        @Override
        public MessageEvent fromBinaryStream(CodedInputStream in) throws IOException {
            return new MessageEvent(in.readSInt(), Type.values()[in.readByte()]);
        }
    }

    @Override
    public String toString() {
        return msg_id + " " + _type;
    }

    @Override
    public void write(CodedBuffer out) {
        out.writeSInt(msg_id);
        out.writeByte(_type.ordinal());
    }
}
