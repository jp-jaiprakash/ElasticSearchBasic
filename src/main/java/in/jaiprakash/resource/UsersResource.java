/**
 * 
 */
package in.jaiprakash.resource;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.network.InetAddresses;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @author Jai Prakash Email: prakashjai01@gmail.com
 *
 */

@RestController
@RequestMapping("/rest/users")
public class UsersResource {

	TransportClient client;

	public UsersResource() {
		try {
			client = new PreBuiltTransportClient(Settings.EMPTY)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@GetMapping("/insert")
	public String insert() {
		try{
			IndexResponse response = client.prepareIndex("employee", "id", "1")
					.setSource(jsonBuilder().startObject().field("name", "jai").field("salary", 89000).endObject()).get();
			return response.getResult().toString();
		}catch(Exception e){
			
		}
		
		return null;
	}
	
	@GetMapping("/view/{id}")
	public Map<String, Object> view(@PathVariable final String id){
		GetResponse getResponse = client.prepareGet("employee", "id", id).get();
		
		return getResponse.getSource();
	}
	
	@GetMapping("/update/{id}")
	public String update(@PathVariable final String id){
		try{
			UpdateRequest updateRequest = new UpdateRequest();
			updateRequest.index("employee")
			.type("id")
			.id(id)
			.doc(jsonBuilder()
					.startObject()
					.field("gender","male")
					.endObject());
			
			UpdateResponse updateResponse = client.update(updateRequest).get();
			return updateResponse.status().toString();
		}catch(Exception e){
			System.out.println(e);
		}
		return null;
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable final String id){
		
		DeleteResponse  deleteresponse = client.prepareDelete("employee", "id",id).get();
		return deleteresponse.getResult().toString();
	}
}
