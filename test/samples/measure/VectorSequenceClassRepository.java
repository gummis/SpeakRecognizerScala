package samples.measure;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class VectorSequenceClassRepository {
	List<VectorSequenceClass> list;
	Map<String,Integer> names;

	public VectorSequenceClassRepository(){
		list = new ArrayList<VectorSequenceClass>();
		names = new HashMap<String,Integer>();
	}
	
	public VectorSequenceClassRepository(File file){
		this();
		load(file);
	}

	public void load(File file) {
		DataInputStream stream  = null;
		try {
			stream = new DataInputStream(
					new BufferedInputStream(
					new GZIPInputStream(
					new FileInputStream(file)  )));
			//==============
			if((stream.readByte()&0xFF) != 0x55){
				throw new IllegalStateException("Błędny format pliku");
			}
			int lengthVector = stream.readByte();
			int lengthClass,lengthSeq;
			if(lengthVector > 0)
				while(stream.available() > 0){
					String name = stream.readUTF();
					List<VectorSequence> vss = new ArrayList<VectorSequence>(lengthClass = stream.readInt());
					for(int x = 0 ; x < lengthClass ; x++){
						List<Vector> ves = new ArrayList<Vector>(lengthSeq = stream.readInt());
						for(int y = 0 ; y < lengthSeq ; y++){
							double[] values = new double[lengthVector];
							for(int z = 0 ; z < values.length ; z++){
								values[z] = stream.readDouble();
							}
							ves.add(new Vector(values));
						}
						vss.add(new VectorSequence(ves));
					}
					list.add(new VectorSequenceClass(name, vss));
					names.put(name, list.size()-1);
				}
			if((stream.readByte()&0xFF) != 0xAA){
				throw new IllegalStateException("Błędny format pliku");
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage(),e);
		} 
		finally{
			if(stream != null){
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void save(File file) {
		DataOutputStream stream  = null;
		try {
			stream = new DataOutputStream(
					new BufferedOutputStream(
					new GZIPOutputStream(
					new FileOutputStream(file)  )));
			//==============
			stream.writeByte(0x55);
			if(list.size() > 0){
				stream.writeInt(list.get(0).get(0).get(0).length());
			}else{
				stream.writeInt(0);
			}
			for(VectorSequenceClass cl : list){
				//zapisanie nazwy
				stream.writeUTF(cl.getName());
				List<VectorSequence> vss = cl.getSequences();
				stream.writeInt(vss.size());
				for(VectorSequence vs : vss){
					List<Vector> ves = vs.getVectors();
					stream.writeInt(ves.size());
					for(Vector ve : ves){
						double[] values = ve.getValues();
						//stream.writeInt(values.length);
						for(double d : values){
							stream.writeDouble(d);
						}
					}
				}
			}
			stream.writeByte(0xAA);
		} catch (IOException e) {
			e.printStackTrace();
			file.delete();
		}finally{
			if(stream != null){
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public int add(VectorSequenceClass seq){
		if(names.containsKey(seq.getName())){
			throw new IllegalStateException("Klasa sekwencji wektorów o nazwie '"+seq.getName()+"' nie moze zostac dodana poniewaz juz istnieje w repozytorium");
		}
		list.add(seq);
		names.put(seq.getName(), list.size()-1);
		return list.size()-1;
	}
	
	public void addSequence(String name,VectorSequence seq){
		Integer i = names.get(name);
		if(i == null){
			i = new Integer(add(new VectorSequenceClass(name, Arrays.asList(new VectorSequence[]{seq}))));
		}else{
			list.get(i).add(seq);
		}
	}
	
	
}
