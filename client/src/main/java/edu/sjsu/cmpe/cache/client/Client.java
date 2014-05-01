package edu.sjsu.cmpe.cache.client;
package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Client {

private final static SortedMap<Integer, String> ring =
new TreeMap<Integer, String>();
private static HashFunction hf = Hashing.md5();
private static ArrayList<String> servers = new ArrayList<String>();
char[] ch = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

public static void main(String[] args) throws Exception {

servers.add("http://localhost:3000");
servers.add("http://localhost:3001");
servers.add("http://localhost:3002");
for(int i=0;i<servers.size();i++)
{
System.out.println("Adding the server : "+servers.get(i));
add(servers.get(i), i);
}

for(int j=0 ; j<10 ; j++)
{
int bucket = Hashing.consistentHash(Hashing.md5().hashInt(j), ring.size());
String server= get(bucket);
System.out.println("routed to Server: " + server);
CacheServiceInterface cache = new DistributedCacheService(server);
cache.put(j+1, String.valuesOfch[j]);
String value = cache.get(j+1);
System.out.println("get("+(j+1)+") => " + value);

}

System.out.println("Exiting Cache Client...");
}



public static void add(String server, int i) {
HashCode hc = hf.hashLong(i);
ring.put(hc.asInt(), server);
}

public static void remove(int key) {
int hash = hf.hashLong(key).asInt();
ring.remove(hash);
}

public static String get(Object key) {
if (ring.isEmpty()) {
return null;
}
int hash = hf.hashLong((Integer)key).asInt();
if (!ring.containsKey(hash)) {
SortedMap<Integer, String> tailMap =
ring.tailMap(hash);
hash = tailMap.isEmpty() ?
ring.firstKey() : tailMap.firstKey();
}
return ring.get(hash);
} 
}
