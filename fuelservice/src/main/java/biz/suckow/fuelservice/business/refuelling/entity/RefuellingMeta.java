package biz.suckow.fuelservice.business.refuelling.entity;

/*
 * #%L
 * fuel
 * %%
 * Copyright (C) 2014 Suckow.biz
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class RefuellingMeta {
    public Date date;
    public String memo;
    public double kilometre;
    public double eurosPerLitre;
    public double litresToTank;
    public double litresToStock;
    public double litresFromStock;
    public boolean isFull;
}
