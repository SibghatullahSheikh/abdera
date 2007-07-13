/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  The ASF licenses this file to You
* under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.  For additional information regarding
* copyright in this work, please see the NOTICE file in the top level
* directory of this distribution.
*/
package org.apache.abdera.protocol.server.impl;

import org.apache.abdera.protocol.ItemManager;
import org.apache.abdera.protocol.Request;
import org.apache.abdera.protocol.server.RequestHandler;

public abstract class AbstractSingletonRequestHandlerManager 
  implements ItemManager<RequestHandler> {

  protected RequestHandler handler;
  
  public RequestHandler get(Request request) {
    if (handler == null) {
      synchronized(this) {
        handler = initHandler();
      }
    }
    return handler;
  }

  protected abstract RequestHandler initHandler();
  
  public void release(RequestHandler item) {
    // nothing to release. subclasses could choose to do reference counting
    // if they want
  }

}