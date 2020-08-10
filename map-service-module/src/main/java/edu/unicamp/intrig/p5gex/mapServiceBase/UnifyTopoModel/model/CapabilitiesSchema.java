package edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.model.Nodes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class CapabilitiesSchema   {
  
  private Nodes supportedNFs = null;

  /**
   **/
  public CapabilitiesSchema supportedNFs(Nodes supportedNFs) {
    this.supportedNFs = supportedNFs;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("supportedNFs")
  public Nodes getSupportedNFs() {
    return supportedNFs;
  }
  public void setSupportedNFs(Nodes supportedNFs) {
    this.supportedNFs = supportedNFs;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CapabilitiesSchema capabilitiesSchema = (CapabilitiesSchema) o;
    return Objects.equals(supportedNFs, capabilitiesSchema.supportedNFs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(supportedNFs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CapabilitiesSchema {\n");
    
    sb.append("    supportedNFs: ").append(toIndentedString(supportedNFs)).append("\n");
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

