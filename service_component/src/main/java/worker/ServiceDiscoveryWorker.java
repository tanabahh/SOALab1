package worker;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import java.util.Collections;

@Singleton
/*
  Registers the app in consul, also performs check in to consul every 15 seconds
 */
public class ServiceDiscoveryWorker {
    private Consul client = null;
    private static final String serviceId = "1";

    {
        try {
            client = Consul.builder().build();
            AgentClient agentClient = client.agentClient();
            Registration service = ImmutableRegistration.builder()
                    .id(serviceId)
                    .name("vehicle-app")
                    .port(8444)
                    .check(Registration.RegCheck.ttl(30L)) // registers with a TTL of 3 seconds
                    .meta(Collections.singletonMap("app", "service_component-1.0-SNAPSHOT"))
                    .build();

            agentClient.register(service);
        } catch (Exception e) {
            System.err.println("Consul is unavailable");
        }
    }

    @Schedule(hour = "*", minute = "*", second = "*/15")
    public void checkIn() throws NotRegisteredException {
        AgentClient agentClient = client.agentClient();
        agentClient.pass(serviceId);
    }

}
