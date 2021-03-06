package edu.unicamp.intrig.p5gex.topologyMapBase.UnifyTopoModel.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.unicamp.intrig.p5gex.topologyMapBase.UnifyTopoModel.model.FlowtableFlowtable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-26T10:14:24.517Z")
public class Flowtable   {
  
  private FlowtableFlowtable flowtable = null;

  /**
   **/
  public Flowtable flowtable(FlowtableFlowtable flowtable) {
    this.flowtable = flowtable;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("flowtable")
  public FlowtableFlowtable getFlowtable() {
    return flowtable;
  }
  public void setFlowtable(FlowtableFlowtable flowtable) {
    this.flowtable = flowtable;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Flowtable flowtable = (Flowtable) o;
    return Objects.equals(flowtable, flowtable.flowtable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(flowtable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Flowtable {\n");
    
    sb.append("    flowtable: ").append(toIndentedString(flowtable)).append("\n");
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

