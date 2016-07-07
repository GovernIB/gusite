package es.caib.gusite.lucene.hibernate;

import java.io.Serializable;
import java.sql.Blob;

public class LuceneFile implements Serializable {

    private String name;
    private long modified;
    private long length;
    private Blob data;


    public LuceneFile() {
        modified = System.currentTimeMillis();
        length = 0L;
        data = null;
    }


    public LuceneFile(String name) {
        modified = System.currentTimeMillis();
        length = 0L;
        data = null;
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public long getModified() {
        return modified;
    }


    public void setModified(long modified) {
        this.modified = modified;
    }


    public long getLength() {
        return length;
    }


    public void setLength(long length) {
        this.length = length;
    }


    public Blob getData() {
        return data;
    }


    public void setData(Blob data) {
        this.data = data;
    }


    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LuceneFile))
            return false;
        LuceneFile luceneFile = (LuceneFile) o;
        if (length != luceneFile.length)
            return false;
        if (modified != luceneFile.modified)
            return false;
        else
            return name != null ? name.equals(luceneFile.name) : luceneFile.name == null;
    }


    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 29 * result + (int) (modified ^ modified >>> 32);
        result = 29 * result + (int) (length ^ length >>> 32);
        return result;
    }


    public Object clone() {
        LuceneFile cloneFile = new LuceneFile(name);
        cloneFile.setModified(modified);
        cloneFile.setLength(length);
        cloneFile.setData(data);
        return cloneFile;
    }

}
