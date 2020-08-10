package edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.model.Flowentry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class FlowtableSchema   {
  
  private List<Flowentry> flowentry = new ArrayList<Flowentry>();

  /**
   **/
  public FlowtableSchema flowentry(List<Flowentry> flowentry) {
    this.flowentry = flowentry;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("flowentry")
  public List<Flowentry> getFlowentry() {
    return flowentry;
  }
  public void setFlowentry(List<Flowentry> flowentry) {
    this.flowentry = flowentry;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FlowtableSchema flowtableSchema = (FlowtableSchema) o;
    return Objects.equals(flowentry, flowtableSchema.flowentry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(flowentry);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FlowtableSchema {\n");
    
    sb.append("    flowentry: ").append(toIndentedString(flowentry)).append("\n");
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

