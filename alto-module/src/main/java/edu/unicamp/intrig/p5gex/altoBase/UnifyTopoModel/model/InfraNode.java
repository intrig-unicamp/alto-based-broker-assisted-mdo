package edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.model.Flowtable;
import edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.model.FlowtableFlowtable;
import edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.model.Nodes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class InfraNode   {
  
  private Object capabilities = null;
  private Nodes nFInstances = null;

  /**
   **/
  public InfraNode capabilities(Object capabilities) {
    this.capabilities = capabilities;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("capabilities")
  public Object getCapabilities() {
    return capabilities;
  }
  public void setCapabilities(Object capabilities) {
    this.capabilities = capabilities;
  }

  /**
   **/
  public InfraNode nFInstances(Nodes nFInstances) {
    this.nFInstances = nFInstances;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("NFInstances")
  public Nodes getNFInstances() {
    return nFInstances;
  }
  public void setNFInstances(Nodes nFInstances) {
    this.nFInstances = nFInstances;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InfraNode infraNode = (InfraNode) o;
    return Objects.equals(capabilities, infraNode.capabilities) &&
        Objects.equals(nFInstances, infraNode.nFInstances);
  }

  @Override
  public int hashCode() {
    return Objects.hash(capabilities, nFInstances);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InfraNode {\n");
    
    sb.append("    capabilities: ").append(toIndentedString(capabilities)).append("\n");
    sb.append("    nFInstances: ").append(toIndentedString(nFInstances)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

