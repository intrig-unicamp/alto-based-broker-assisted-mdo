package edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;

public class RFC7285JSONMapper {

    private ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(Include.NON_DEFAULT)
            .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

    public Endpoint.AddressGroup asAddressGroup(String json) throws Exception {
        return mapper.readValue(json, Endpoint.AddressGroup.class);
    }

    public Endpoint.PropertyRequest asPropertyRequest(String json) throws Exception {
        Endpoint.PropertyRequest ret = mapper.readValue(json, Endpoint.PropertyRequest.class);

        if (ret.properties == null) {
            throw new JsonMappingException("Missing field:properties");
        }
        if (ret.endpoints == null) {
            throw new JsonMappingException("Missing field:endpoints");
        }
        return ret;
    }

    public Endpoint.PropertyResponse asPropertyResponse(String json) throws Exception {
        return mapper.readValue(json, Endpoint.PropertyResponse.class);
    }

    public Endpoint.CostRequest asCostRequest(String json) throws Exception {
        Endpoint.CostRequest ret = mapper.readValue(json, Endpoint.CostRequest.class);
        if (ret.costType == null) {
            throw new JsonMappingException("Missing field:cost-type");
        }
        if (ret.endpoints == null) {
            throw new JsonMappingException("Missing field:endpoints");
        }
        return ret;
    }

    public Endpoint.CostResponse asCostResponse(String json) throws Exception {
        return mapper.readValue(json, Endpoint.CostResponse.class);
    }

    public CostMap asCostMap(String json) throws Exception {
        return mapper.readValue(json, CostMap.class);
    }

    public List<CostMap> asCostMapList(String json) throws Exception {
        return Arrays.asList(mapper.readValue(json, CostMap[].class));
    }

    public CostType asCostType(String json) throws Exception {
        return mapper.readValue(json, CostType.class);
    }

    public Endpoint asEndpoint(String json) throws Exception {
        return mapper.readValue(json, Endpoint.class);
    }

    public Extensible asExtensible(String json) throws Exception {
        return mapper.readValue(json, Extensible.class);
    }

    public RFC7285IRD asIRD(String json) throws Exception {
        return mapper.readValue(json, RFC7285IRD.class);
    }

    public RFC7285NetworkMap asNetworkMap(String json) throws Exception {
        return mapper.readValue(json, RFC7285NetworkMap.class);
    }

    public List<RFC7285NetworkMap> asNetworkMapList(String json) throws Exception {
        return Arrays.asList(mapper.readValue(json, RFC7285NetworkMap[].class));
    }

    public RFC7285NetworkMap.Filter asNetworkMapFilter(String json) throws Exception {
        RFC7285NetworkMap.Filter ret = mapper.readValue(json, RFC7285NetworkMap.Filter.class);
        if (ret.pids == null) {
            throw new JsonMappingException("Missing field:pids");
        }
        return ret;
    }
    
    public PropertyMap.Filter asPropertyMapFilter(String json) throws Exception {
        PropertyMap.Filter ret = mapper.readValue(json, PropertyMap.Filter.class);
        if (ret.pids == null) {
            throw new JsonMappingException("Missing field:pids");
        }
        return ret;
    }

    public CostMap.Filter asCostMapFilter(String json) throws Exception {
        CostMap.Filter ret = mapper.readValue(json, CostMap.Filter.class);
        if (ret.costType == null) {
            throw new JsonMappingException("Missing field:cost-type");
        }
        if (ret.pids == null) {
            throw new JsonMappingException("Missing field:pids");
        }
        return ret;
    }

    public VersionTag asVersionTag(String json) throws Exception {
        return mapper.readValue(json, VersionTag.class);
    }

    public RFC7285EndpointPropertyMap asEndpointPropMap(String json) throws Exception {
        return mapper.readValue(json, RFC7285EndpointPropertyMap.class);
    }

    public String asJSON(Object obj) throws Exception {
        return mapper.writeValueAsString(obj);
    }
}