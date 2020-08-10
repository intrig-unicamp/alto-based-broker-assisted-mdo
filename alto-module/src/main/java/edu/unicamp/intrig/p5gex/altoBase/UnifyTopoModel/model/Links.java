package edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.model.LinksLinks;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class Links   {
  
  private LinksLinks links = null;

  /**
   **/
  public Links links(LinksLinks links) {
    this.links = links;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("links")
  public LinksLinks getLinks() {
    return links;
  }
  public void setLinks(LinksLinks links) {
    this.links = links;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Links links = (Links) o;
    return Objects.equals(links, links.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(links);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Links {\n");
    
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

