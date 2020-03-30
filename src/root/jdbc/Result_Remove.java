package root.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;

public class Result_Remove implements Iterable<Result_Remove> {

	private boolean closed;
	private final ResultSet resultSet;

	Result_Remove(final ResultSet resultSet) {
		this.resultSet = resultSet;
	}

//	@Override
//	public ResultSetMetaData getMetaData() throws SQLException {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public boolean exists() {
		if (!closed) {
			try {
				final boolean exists = resultSet.next();

				closed = true;
				resultSet.close();

				return exists;
			} catch (SQLException e) {
				throw new DatabaseException(e);
			}
		}

		return false;
	}

	public InputStream getAsciiStream(final int columnIndex) {
		try {
			return resultSet.getAsciiStream(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public InputStream getAsciiStream(final String columnLabel) {
		try {
			return resultSet.getAsciiStream(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public BigDecimal getBigDecimal(final int columnIndex) {
		try {
			return resultSet.getBigDecimal(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public BigDecimal getBigDecimal(final String columnLabel) {
		try {
			return resultSet.getBigDecimal(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public InputStream getBinaryStream(final int columnIndex) {
		try {
			return resultSet.getBinaryStream(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public InputStream getBinaryStream(final String columnLabel) {
		try {
			return resultSet.getBinaryStream(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public Blob getBlob(final int columnIndex) {
		try {
			return resultSet.getBlob(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public Blob getBlob(final String columnLabel) {
		try {
			return resultSet.getBlob(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public boolean getBoolean(final int columnIndex) {
		try {
			return resultSet.getBoolean(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public boolean getBoolean(final String columnLabel) {
		try {
			return resultSet.getBoolean(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public byte getByte(final int columnIndex) {
		try {
			return resultSet.getByte(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public byte getByte(final String columnLabel) {
		try {
			return resultSet.getByte(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public byte[] getBytes(final int columnIndex) {
		try {
			return resultSet.getBytes(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public byte[] getBytes(final String columnLabel) {
		try {
			return resultSet.getBytes(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public Reader getCharacterStream(final int columnIndex) {
		try {
			return resultSet.getCharacterStream(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public Reader getCharacterStream(final String columnLabel) {
		try {
			return resultSet.getCharacterStream(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public Clob getClob(final int columnIndex) {
		try {
			return resultSet.getClob(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public Clob getClob(final String columnLabel) {
		try {
			return resultSet.getClob(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public Date getDate(final int columnIndex) {
		try {
			return resultSet.getDate(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public Date getDate(final String columnLabel) {
		try {
			return resultSet.getDate(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public double getDouble(final int columnIndex) {
		try {
			return resultSet.getDouble(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public double getDouble(final String columnLabel) {
		try {
			return resultSet.getDouble(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public float getFloat(final int columnIndex) {
		try {
			return resultSet.getFloat(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public float getFloat(final String columnLabel) {
		try {
			return resultSet.getFloat(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public int getInt(final int columnIndex) {
		try {
			return resultSet.getInt(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public int getInt(final String columnLabel) {
		try {
			return resultSet.getInt(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public long getLong(final int columnIndex) {
		try {
			return resultSet.getLong(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public long getLong(final String columnLabel) {
		try {
			return resultSet.getLong(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public Object getObject(final int columnIndex) {
		try {
			return resultSet.getObject(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public Object getObject(final String columnLabel) {
		try {
			return resultSet.getObject(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public short getShort(final int columnIndex) {
		try {
			return resultSet.getShort(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public short getShort(final String columnLabel) {
		try {
			return resultSet.getShort(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public String getString(final int columnIndex) {
		try {
			return resultSet.getString(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public String getString(final String columnLabel) {
		try {
			return resultSet.getString(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public Time getTime(final int columnIndex) {
		try {
			return resultSet.getTime(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public Time getTime(final String columnLabel) {
		try {
			return resultSet.getTime(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public Timestamp getTimestamp(final int columnIndex) {
		try {
			return resultSet.getTimestamp(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public Timestamp getTimestamp(final String columnLabel) {
		try {
			return resultSet.getTimestamp(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public URL getURL(final int columnIndex) {
		try {
			return resultSet.getURL(columnIndex);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public URL getURL(final String columnLabel) {
		try {
			return resultSet.getURL(columnLabel);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public Iterator<Result_Remove> iterator() {
		return new Itr();
	}

	//  <><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><><>


	private class Itr implements Iterator<Result_Remove> {

		private boolean hasNext = !closed;

		@Override
		public boolean hasNext() {
			if (hasNext) {
				try {
					hasNext = resultSet.next();

					if (!hasNext) {
						closed = true;
						resultSet.close();
					}
				} catch (SQLException e) {
					throw new DatabaseException(e);
				}
			}

			return hasNext;
		}

		@Override
		public Result_Remove next() {
			return Result_Remove.this;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}	// End Itr

}	// End Result
