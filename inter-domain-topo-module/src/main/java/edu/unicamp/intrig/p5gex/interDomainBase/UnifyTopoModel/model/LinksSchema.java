package edu.unicamp.intrig.p5gex.interDomainBase.UnifyTopoModel.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.unicamp.intrig.p5gex.interDomainBase.UnifyTopoModel.model.Link;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-26T10:14:24.517Z")
public class LinksSchema   {
  
  private List<Link> link = new ArrayList<Link>();

  /**
   **/
  public LinksSchema link(List<Link> link) {
    this.link = link;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("link")
  public List<Link> getLink() {
    return link;
  }
  public void setLink(List<Link> link) {
    this.link = link;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinksSchema linksSchema = (LinksSchema) o;
    return Objects.equals(link, linksSchema.link);
  }

  @Override
  public int hashCode() {
    return Objects.hash(link);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LinksSchema {\n");
    
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
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

