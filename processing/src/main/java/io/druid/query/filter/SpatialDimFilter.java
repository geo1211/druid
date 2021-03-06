/*
 * Druid - a distributed column store.
 * Copyright 2012 - 2015 Metamarkets Group Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.druid.query.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.metamx.collections.spatial.search.Bound;
import com.metamx.common.StringUtils;

import java.nio.ByteBuffer;

/**
 */
public class SpatialDimFilter implements DimFilter
{
  private final String dimension;
  private final Bound bound;

  @JsonCreator
  public SpatialDimFilter(
      @JsonProperty("dimension") String dimension,
      @JsonProperty("bound") Bound bound
  )
  {
    Preconditions.checkArgument(dimension != null, "dimension must not be null");
    Preconditions.checkArgument(bound != null, "bound must not be null");

    this.dimension = dimension;
    this.bound = bound;
  }

  @Override
  public byte[] getCacheKey()
  {
    byte[] dimBytes = StringUtils.toUtf8(dimension);
    byte[] boundBytes = bound.getCacheKey();

    return ByteBuffer.allocate(2 + dimBytes.length + boundBytes.length)
                     .put(DimFilterCacheHelper.SPATIAL_CACHE_ID)
                     .put(dimBytes)
                     .put(DimFilterCacheHelper.STRING_SEPARATOR)
                     .put(boundBytes)
                     .array();
  }

  @JsonProperty
  public String getDimension()
  {
    return dimension;
  }

  @JsonProperty
  public Bound getBound()
  {
    return bound;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SpatialDimFilter that = (SpatialDimFilter) o;

    if (bound != null ? !bound.equals(that.bound) : that.bound != null) {
      return false;
    }
    if (dimension != null ? !dimension.equals(that.dimension) : that.dimension != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = dimension != null ? dimension.hashCode() : 0;
    result = 31 * result + (bound != null ? bound.hashCode() : 0);
    return result;
  }

  @Override
  public String toString()
  {
    return "SpatialDimFilter{" +
           "dimension='" + dimension + '\'' +
           ", bound=" + bound +
           '}';
  }
}
