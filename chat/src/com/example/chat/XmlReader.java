package com.example.chat;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.google.android.gms.maps.model.LatLng;

import android.util.Log;
import android.util.Xml;

public class XmlReader {
	private static final String TAG = "XmlReader.java";
	private String httpData = null;
	
	public XmlReader(String httpData) {
		this.httpData = httpData;
	}
	
	public static HashMap<String, String> getNoDataMap() {
		HashMap<String, String> noDataMap = new HashMap<String, String>();
		noDataMap.put("", "");
		noDataMap.put("GuideId", "-1");
		noDataMap.put("GuideName", "‚µ‚¨‚è‚ª‚ ‚è‚Ü‚¹‚ñ");
		noDataMap.put("Season", "");
		return noDataMap;
	}
	
	public ArrayList<HashMap<String, String>> getFavoriteSpotId(){
    	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    	
    	if (httpData == null) {
            return list;
        }
        
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(httpData));
            int eventType = parser.getEventType();
            HashMap<String, String> map = null;
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                    	if ("FavoriteSpot".equals(parser.getName())) {
                            map = new HashMap<String, String>();
                        } else if ("FavoriteSpotId".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("SpotId".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("Name".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("Lat".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("Lng".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("FavoriteSpot".equals(parser.getName())) {
                            list.add(map);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        }catch(IllegalStateException e) {
            Log.e("TAG", e.toString());
        }catch(IOException e) {
            Log.e("TAG", e.toString());
        }catch(XmlPullParserException e) {
            Log.e("TAG", e.toString());
        }
        return list;
    }
	
	public ArrayList<Spot> getSpotData() {
    	ArrayList<Spot> list = new ArrayList<Spot>();
    	
    	if (httpData == null) {
            return list;
        }
    	
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(httpData));
            int eventType = parser.getEventType();
            Spot spot = null;
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                	break;
                case XmlPullParser.START_TAG:
                	if ("Spot".equals(parser.getName())) {
                		spot = new Spot();
                    } else if ("FavoriteId".equals(parser.getName())) {
                    	parser.next();
                    	String id = parser.getText();
                    	if (id != null) {
                    		spot.favoriteID = Integer.parseInt(id);
                    	}            
                    } else if ("SpotId".equals(parser.getName())) {
                    	parser.next();
                        spot.spotID = Integer.parseInt(parser.getText());
                    } else if ("Name".equals(parser.getName())) {
                        parser.next();
                        spot.name = parser.getText();
                    } else if ("Exp".equals(parser.getName())) {
                        parser.next();
                        spot.exp = parser.getText();
                    } else if ("Address".equals(parser.getName())) {
                    	parser.next();
                        spot.address = parser.getText();
                    } else if ("Tel".equals(parser.getName())) {
                    	parser.next();
                        spot.tel = parser.getText();
                    } else if ("Access".equals(parser.getName())) {
                    	parser.next();
                        spot.access = parser.getText();
                    } else if ("SampleRate".equals(parser.getName())) {
                    	parser.next();
                        spot.fee = parser.getText();
                    } else if ("OpeningHours".equals(parser.getName())) {
                    	parser.next();
                        spot.openingHours = parser.getText();
                    } else if ("Category".equals(parser.getName())) {
                    	parser.next();
                        spot.categoryName = parser.getText();
                    } else if ("Area".equals(parser.getName())) {
                    	parser.next();
                        spot.areaName = parser.getText();
                    } else if ("Lat".equals(parser.getName())) {
                    	parser.next();
                        spot.lat = Double.parseDouble(parser.getText());
                    } else if ("Lng".equals(parser.getName())) {
                    	parser.next();
                        spot.lng = Double.parseDouble(parser.getText());
                    } else if ("Image".equals(parser.getName())) {
                    	parser.next();
                        spot.imageUrl = "http://kproject.php.xdomain.jp/chat/image/spot/" + parser.getText() + ".jpg";
                        if (!parser.getText().equals("spot")) {
                        	spot.hasImage = true;
                        }
                    }
                        
                    break;
                case XmlPullParser.END_TAG:
                	if ("Spot".equals(parser.getName())) {
                		list.add(spot);
                	}
                        
                    break;
                default:
                	break;
                }
                eventType = parser.next();
            }
        } catch (IllegalStateException e) {
        	Log.e(TAG, e.toString());
        } catch (IOException e) {
        	Log.e(TAG, e.toString());
        } catch (XmlPullParserException e) {
        	Log.e(TAG, e.toString());
        }
        return list;
    }
	
	public ArrayList<HashMap<String, String>> getFavoriteHotelId(){
    	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    	
    	if (httpData == null) {
            return list;
        }
        
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(httpData));
            int eventType = parser.getEventType();
            HashMap<String, String> map = null;
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                    	if ("FavoriteHotel".equals(parser.getName())) {
                            map = new HashMap<String, String>();
                        } else if ("FavoriteHotelId".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("HotelId".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("Name".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("Lat".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("Lng".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("FavoriteHotel".equals(parser.getName())) {
                            list.add(map);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        }catch(IllegalStateException e) {
            Log.e("TAG", e.toString());
        }catch(IOException e) {
            Log.e("TAG", e.toString());
        }catch(XmlPullParserException e) {
            Log.e("TAG", e.toString());
        }
        return list;
    }
	
	public ArrayList<Hotel> getHotelData() {
		ArrayList<Hotel> list = new ArrayList<Hotel>();
	   	
	   	if (httpData == null) {
	           return list;
	   	}
	   	
	   	try {
	   		XmlPullParser parser = Xml.newPullParser();
	   		parser.setInput(new StringReader(httpData));
	   		int eventType = parser.getEventType();
	   		Hotel hotelinfo = null;
	   		
	   		while (eventType != XmlPullParser.END_DOCUMENT) {
	   			switch (eventType) {
	   			case XmlPullParser.START_DOCUMENT:
	   				break;
	   			case XmlPullParser.START_TAG:
	   				if ("Hotel".equals(parser.getName())) {
	   					hotelinfo = new Hotel();
	   				} else if("HotelID".equals(parser.getName())) {
	   					parser.next();
	   					hotelinfo.hotelID = Integer.parseInt(parser.getText());
	   				} else if("HotelName".equals(parser.getName())) {
	   					parser.next();
	   					hotelinfo.name = parser.getText();
	   				} else if("PostCode".equals(parser.getName())) {
	   					parser.next();
	   					hotelinfo.postCode = parser.getText();
	   				} else if("HotelAddress".equals(parser.getName())) {
	   					parser.next();
	   					hotelinfo.address = parser.getText();
	   				} else if("HotelType".equals(parser.getName())) {
	   					parser.next();
	   					hotelinfo.hotelType = parser.getText();
	   				} else if("HotelDetailURL".equals(parser.getName())) {
	   					parser.next();
	   					hotelinfo.hotelURL = parser.getText();
	   				} else if("HotelCatchCopy".equals(parser.getName())) {
	   					parser.next();
	   					hotelinfo.catchCopy = parser.getText();
	   				} else if("HotelCaption".equals(parser.getName())) {
	   					parser.next();
	   					hotelinfo.hotelCaption = parser.getText();
	   				} else if("PictureURL".equals(parser.getName())) {
	   					parser.next();
	   					hotelinfo.imageUrl = parser.getText();
	   					if (!parser.getText().equals("")) {
                        	hotelinfo.hasImage = true;
                        }
	                } else if("PictureCaption".equals(parser.getName())) {
	                	parser.next();
	                	hotelinfo.imageCaption = parser.getText();
	                } else if("CheckInTime".equals(parser.getName())) {
	                	parser.next();
	                	hotelinfo.check_in = parser.getText();
	                } else if("CheckOutTime".equals(parser.getName())) {
	                	parser.next();
	                	hotelinfo.check_out = parser.getText();
	                } else if("X".equals(parser.getName())) {
	                	parser.next();
	                	hotelinfo.x = parser.getText();
	                } else if("Y".equals(parser.getName())) {
	                	parser.next();
	                	hotelinfo.y = parser.getText();
	                } else if("Region".equals(parser.getName())) {
	                	parser.next();
	                	hotelinfo.region = parser.getText();
	                } else if("Prefecture".equals(parser.getName())) {
	                	parser.next();
	                	hotelinfo.prefecture = parser.getText();
	                } else if("LargeArea".equals(parser.getName())) {
	                	parser.next();
	                	hotelinfo.largeArea = parser.getText();
	                } else if("SmallArea".equals(parser.getName())) {
	                	parser.next();
	                	hotelinfo.smallArea = parser.getText();
	                } else if("AccessInformation".equals(parser.getName())) {
	                	hotelinfo.accessInformationTitle.add(parser.getAttributeValue(0));
	                	parser.next();
	                	hotelinfo.accessInformation.add(parser.getText());
	                } else if("SampleRateFrom".equals(parser.getName())) {
	                	parser.next();
	                	hotelinfo.sampleRate = parser.getText();
	                }
	   				
	   				break;
	   			case XmlPullParser.END_TAG:
	   				if ("Hotel".equals(parser.getName())) {
	   					hotelinfo.changeXY();
	   					list.add(hotelinfo);
	   				}
	   				break;
	   			default:
	   				break;
	   			}
	   			eventType = parser.next();
	   	 	}
	   	} catch(IllegalStateException e) {
	   		Log.e("TAG", e.toString());
	   	} catch(IOException e) {
		    Log.e("TAG", e.toString());
	    } catch(XmlPullParserException e) {
		    Log.e("TAG", e.toString());
	    }
	   
	    return list;
	}
	
	public ArrayList<Plan> getPlanData(){
    	ArrayList<Plan> list = new ArrayList<Plan>();
    	
    	if (httpData == null) {
            return list;
        }
    	
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(httpData));
            int eventType = parser.getEventType();
            Plan plan = null;
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("Plan".equals(parser.getName())) {
                            plan=new Plan();
                        } else if ("PlanName".equals(parser.getName())) {
                        	parser.next();
                        	plan.PlanName = parser.getText();
                        } else if ("PlanCD".equals(parser.getName())) {
                        	parser.next();
                        	plan.PlanCD = parser.getText();
                        } else if ("RoomType".equals(parser.getName())) {
                        	parser.next();
                        	plan.RoomType.add(parser.getText());
                        } else if ("RoomName".equals(parser.getName())) {
                        	parser.next();
                        	plan.RoomName = parser.getText();
                        } else if ("PlanCheckIn".equals(parser.getName())) {
                        	parser.next();
                        	plan.PlanCheckIn = parser.getText();
                        } else if ("PlanCheckOut".equals(parser.getName())) {
                        	parser.next();
                        	plan.PlanCheckOut = parser.getText();
                        } else if ("PlanDetailURL".equals(parser.getName())) {
                        	parser.next();
                        	plan.PlanDetailURL = parser.getText();
                        } else if ("PlanPictureURL".equals(parser.getName())) {
                        	parser.next();
                        	plan.PlanPictureURL = parser.getText();
                        } else if ("PlanPictureCaption".equals(parser.getName())) {
                        	parser.next();
                        	plan.PlanPictureCaption = parser.getText();
                        } else if ("Meal".equals(parser.getName())) {
                        	parser.next();
                        	plan.Meal = parser.getText();
                        } else if ("PlanSampleRateFrom".equals(parser.getName())) {
                        	parser.next();
                        	plan.PlanSampleRateFrom = parser.getText();
                        }
                        
                        break;
                    case XmlPullParser.END_TAG:
                        if ("Plan".equals(parser.getName())) {
                            list.add(plan);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch(IllegalStateException e) {
            Log.e("TAG", e.toString());
        } catch(IOException e) {
            Log.e("TAG", e.toString());
        } catch(XmlPullParserException e) {
            Log.e("TAG", e.toString());
        }
    	
        return list;
    }
	
	public ArrayList<Area> getAreaData(){
    	ArrayList<Area> list = new ArrayList<Area>();
    	
    	if (httpData == null) {
            return list;
        }
        
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(httpData));
            int eventType = parser.getEventType();
            Area areainfo = null;
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("Prefecture".equals(parser.getName())) {
                            areainfo = new Area(parser.getAttributeValue(1), parser.getAttributeValue(0));
                        } else if ("LargeArea".equals(parser.getName())) {
                        	areainfo.addLarge(parser.getAttributeValue(1), parser.getAttributeValue(0));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("Prefecture".equals(parser.getName())) {
                            list.add(areainfo);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        }catch(IllegalStateException e) {
            Log.e("TAG", e.toString());
        }catch(IOException e) {
            Log.e("TAG", e.toString());
        }catch(XmlPullParserException e) {
            Log.e("TAG", e.toString());
        }
        return list;
    }
	
	public ArrayList<HashMap<String, String>> getFavoriteGourmetId(){
    	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    	
    	if (httpData == null) {
            return list;
        }
        
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(httpData));
            int eventType = parser.getEventType();
            HashMap<String, String> map = null;
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                    	if ("FavoriteGourmet".equals(parser.getName())) {
                            map = new HashMap<String, String>();
                        } else if ("FavoriteGourmetId".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("GourmetId".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("Name".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("Lat".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("Lng".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("FavoriteGourmet".equals(parser.getName())) {
                            list.add(map);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        }catch(IllegalStateException e) {
            Log.e("TAG", e.toString());
        }catch(IOException e) {
            Log.e("TAG", e.toString());
        }catch(XmlPullParserException e) {
            Log.e("TAG", e.toString());
        }
    	
        return list;
    }
	
	public ArrayList<Gourmet> getGourmetLocaTouch() {
    	ArrayList<Gourmet> list = new ArrayList<Gourmet>();
    	
    	if (httpData == null) {
            return list;
        }
        
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(httpData));
            int eventType = parser.getEventType();
            Gourmet gourmet = null;
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                    	if ("restaurant".equals(parser.getName())) {
                    		gourmet = new Gourmet();
                        } else if ("item_id".equals(parser.getName())) {
                        	eventType = parser.next();
                        	gourmet.gourmetId = parser.getText();
                        } else if ("name".equals(parser.getName())) {
                        	eventType = parser.next();
                        	if (gourmet.name.equals("")) {
                        		gourmet.name = parser.getText();
                        	}
                        } else if ("property".equals(parser.getName())) {
                        	eventType = parser.next();
                        	gourmet.property  = parser.getText();
                        } else if ("north_latitude".equals(parser.getName())) {
                        	eventType = parser.next();
                        	if (parser.getText() != null) {
                        		gourmet.lat = Double.parseDouble(parser.getText());
                        	} else {
                        		gourmet.lat = 1.1;
                        	}
                        } else if ("east_longitude".equals(parser.getName())) {
                        	eventType = parser.next();
                        	if (parser.getText() != null) {
                        		gourmet.lng = Double.parseDouble(parser.getText());
                        	} else {
                        		gourmet.lng = 1.1;
                        	}
                        } else if ("category".equals(parser.getName())) {
                        	for (int i = 0; i < 8; i++) {
                        		eventType = parser.next();
                        		if (eventType == XmlPullParser.START_TAG) {
                        			if ("name".equals(parser.getName())) {
                        				parser.next();
                                		gourmet.category = parser.getText();
                        			}
                        		}
                        	}
                        } else if ("url".equals(parser.getName())) {
                        	if (gourmet != null) {
                        		if (gourmet.url.equals("")) {
                        			eventType = parser.next();
                        			String url = parser.getText();
                        			if (url == null) {
                        				gourmet.url = "-";
                        			} else {
                        				gourmet.url = url;
                        			}
                        		}
                        	}
                        } else if ("address".equals(parser.getName())) {
                        	eventType = parser.next();
                        	gourmet.address = parser.getText();
                        } else if ("tel".equals(parser.getName())) {
                        	eventType = parser.next();
                        	gourmet.tel = parser.getText();
                        } else if ("fax".equals(parser.getName())) {
                        	eventType = parser.next();
                        	gourmet.fax = parser.getText();
                        } else if ("opentime".equals(parser.getName())) {
                        	eventType = parser.next();
                        	gourmet.openTime = parser.getText();
                        } else if ("weekday".equals(parser.getName())) {
                        	eventType = parser.next();
                        	gourmet.weekday = parser.getText();
                        } else if ("saturday".equals(parser.getName())) {
                        	eventType = parser.next();
                        	gourmet.saturday = parser.getText();
                        } else if ("holiday".equals(parser.getName())) {
                        	eventType = parser.next();
                        	gourmet.holiday = parser.getText();
                        } else if ("station".equals(parser.getName())) {
                        	for (int i = 0; i < 16; i++) {
                        		eventType = parser.next();
                        		if (eventType == XmlPullParser.START_TAG) { 
                        			if ("name".equals(parser.getName())) {
                        				parser.next();
                                		gourmet.access = parser.getText() + "‰w";
                        			} else if ("time".equals(parser.getName())) {
                        				parser.next();
                        				gourmet.access += parser.getText() + "•ª";
                            		} else if ("distance".equals(parser.getName())) {
                            			parser.next();
                                		gourmet.access += "–ñ" + parser.getText() + "m";
                            		}
                        		}
                        	}
                        } else if ("lunch".equals(parser.getName())) {
                        	if (gourmet != null) {
                        		if (gourmet.lunch.equals("")) {
                        			eventType = parser.next();
                        			String lunch = parser.getText();
                        			if (lunch == null) {
                        				gourmet.lunch = "-";
                        			} else {
                        				gourmet.lunch = lunch;
                        			}
                        		}
                        	}
                        } else if ("dinner".equals(parser.getName())) {
                        	eventType = parser.next();
                        	gourmet.dinner = parser.getText();
                        } else if ("total".equals(parser.getName())) {
                        	eventType = parser.next();
                        	gourmet.rate = parser.getText();
                        } else if ("days".equals(parser.getName())) {
                        	eventType = parser.next();
                        	gourmet.close = parser.getText();
                        } else if ("price".equals(parser.getName())) {
                        	eventType = parser.next();
                        	gourmet.sampleRate = parser.getText();
                        } else if ("photo".equals(parser.getName())) {
                        	for (int i = 0; i < 16; i++) {
                        		eventType = parser.next();
                        		if (eventType == XmlPullParser.START_TAG) { 
                        			if ("url".equals(parser.getName())) {
                        				parser.next();
                                		gourmet.imageURL = parser.getText();
                                		if (!parser.getText().equals("")) {
                                        	gourmet.hasImage = true;
                                        }
                        			}
                        		}
                        	}
                        } 
                        break;
                    case XmlPullParser.END_TAG:
                        if ("restaurant".equals(parser.getName())) {
                            list.add(gourmet);
                        }
                        break;
                    default:
                        break;
                }
                
                eventType = parser.next();
            }
        }catch(IllegalStateException e) {
            Log.e("TAG", e.toString());
        }catch(IOException e) {
            Log.e("TAG", e.toString());
        }catch(XmlPullParserException e) {
            Log.e("TAG", e.toString());
        }
    	
    	if (list.size() == 1) {
    		if (list.get(0).gourmetId == null) {
    			list.remove(0);
    		}
    	}
    	
        return list;
    }
	
	public ArrayList<HashMap<String, String>> getGuideId() {
    	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    	
    	if (httpData == null) {
            return list;
        }
        
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(httpData));
            int eventType = parser.getEventType();
            HashMap<String, String> map = null;
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                    	if ("Guide".equals(parser.getName())) {
                    		map = new HashMap<String, String>();
                        } else if ("GuideId".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("GuideName".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("HotelId".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("Memo".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("StartDay".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("EndDay".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        } else if ("Season".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	map.put(key, parser.getText());
                        	Log.d("season", parser.getText() + "");
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("Guide".equals(parser.getName())) {
                            list.add(map);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        }catch(IllegalStateException e) {
            Log.e("TAG", e.toString());
        }catch(IOException e) {
            Log.e("TAG", e.toString());
        }catch(XmlPullParserException e) {
            Log.e("TAG", e.toString());
        }
        return list;
    }
	
	public ArrayList<HashMap<String, String>> getGuideDayId(){
    	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    	
    	if (httpData == null) {
            return list;
        }
        
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(httpData));
            int eventType = parser.getEventType();
            HashMap<String, String> pair = null;
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                    	if ("GuideDay".equals(parser.getName())) {
                            pair = new HashMap<String, String>();
                        } else if ("GuideDayId".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	pair.put(key, parser.getText());
                        } else if ("Day".equals(parser.getName())) {
                        	String key = parser.getName();
                        	parser.next();
                        	pair.put(key, parser.getText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("GuideDay".equals(parser.getName())) {
                            list.add(pair);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        }catch(IllegalStateException e) {
            Log.e("TAG", e.toString());
        }catch(IOException e) {
            Log.e("TAG", e.toString());
        }catch(XmlPullParserException e) {
            Log.e("TAG", e.toString());
        }
        return list;
    }
	
	public ArrayList<Route> getRoute(){
    	ArrayList<Route> list = new ArrayList<Route>();
    	
    	if (httpData == null) {
            return list;
        }
        
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(httpData));
            int eventType = parser.getEventType();
            Route route = null;
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                    	if ("Route".equals(parser.getName())) {
                            route = new Route();
                            route.destPoint = new Point();
                        } else if ("RouteId".equals(parser.getName())) {
                        	parser.next();
                        	route.routeId = parser.getText();
                        } else if ("Priority".equals(parser.getName())) {
                        	parser.next();
                        	route.priority = Integer.parseInt(parser.getText());
                        } else if ("StayTimeSec".equals(parser.getName())) {
                        	parser.next();
                        	route.stayTimeSec = Integer.parseInt(parser.getText());
                        } else if ("StartTime".equals(parser.getName())) {
                        	parser.next();
                        	route.startTime = parser.getText();
                        } else if ("EndTime".equals(parser.getName())) {
                        	parser.next();
                        	route.endTime = parser.getText();
                        } else if ("StartTimeSec".equals(parser.getName())) {
                        	parser.next();
                        	route.startTimeSec = Integer.parseInt(parser.getText());
                        } else if ("EndTimeSec".equals(parser.getName())) {
                        	parser.next();
                        	route.endTimeSec = Integer.parseInt(parser.getText());
                        } else if ("Duration".equals(parser.getName())) {
                        	parser.next();
                        	route.duration = parser.getText();
                        } else if ("DurationSec".equals(parser.getName())) {
                        	parser.next();
                        	route.durationSec = Integer.parseInt(parser.getText());
                        } else if ("Distance".equals(parser.getName())) {
                        	parser.next();
                        	route.distance = parser.getText();
                        }else if ("SpotId".equals(parser.getName())) {
                        	parser.next();
                        	route.destPoint.id = parser.getText();
                        } else if ("SpotName".equals(parser.getName())) {
                        	parser.next();
                        	if (parser.getText() != null) {
                        		route.destPoint.name = parser.getText();
                        	}
                        } else if ("SpotLat".equals(parser.getName())) {
                        	parser.next();
                        	if (parser.getText() != null) { 
                        		route.destPoint.lat = Double.parseDouble(parser.getText());
                        	}
                        } else if ("SpotLng".equals(parser.getName())) {
                        	parser.next();
                        	if (parser.getText() != null) {
                        		route.destPoint.lng = Double.parseDouble(parser.getText());
                        	}
                        } else if ("Kind".equals(parser.getName())) {
                        	parser.next();
                        	if (parser.getText() != null) {
                        		route.destPoint.kind = Integer.parseInt(parser.getText());
                        	}
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("Route".equals(parser.getName())) {
                            list.add(route);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        }catch(IllegalStateException e) {
            Log.e("TAG", e.toString());
        }catch(IOException e) {
            Log.e("TAG", e.toString());
        }catch(XmlPullParserException e) {
            Log.e("TAG", e.toString());
        }
        return list;
    }
	
	public Route getGoogleMapRoute() {
    	Route mainRoute = new Route();
    	LatLng endLatLng = null;
    	
    	if (httpData == null) {
            return mainRoute;
        }
    	
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(httpData));
            int eventType = parser.getEventType();
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                	break;
                case XmlPullParser.START_TAG:
                	if ("start_location".equals(parser.getName())) {
                		moveParser(parser, 3);
                		double lat = Double.parseDouble(parser.getText());
                		moveParser(parser, 4);
                		double lng = Double.parseDouble(parser.getText());    
                		mainRoute.routeLatLng.add(new LatLng(lat, lng));
                	} else if ("end_location".equals(parser.getName())) {
                		moveParser(parser, 3);
                		double lat = Double.parseDouble(parser.getText());
                		moveParser(parser, 4);
                		double lng = Double.parseDouble(parser.getText());
                		//mainRoute.routeLatLng.add(new LatLng(lat, lng));
                		endLatLng = new LatLng(lat, lng);
                	} else if ("duration".equals(parser.getName())) {
                		moveParser(parser, 3);
                		mainRoute.durationSec = Integer.parseInt(parser.getText());
                		moveParser(parser, 4);
                		mainRoute.duration = parser.getText();
                	} else if ("distance".equals(parser.getName())) {
                		moveParser(parser, 7);
                		mainRoute.distance = parser.getText();
                	}
                case XmlPullParser.END_TAG:
                    break;
                default:
                	break;
                }
                eventType = parser.next();        
            }
        } catch (IllegalStateException e) {
        	Log.e(TAG, e.toString());
        } catch (IOException e) {
        	Log.e(TAG, e.toString());
        } catch (XmlPullParserException e) {
        	Log.e(TAG, e.toString());
        }
    	
    	if (mainRoute.routeLatLng.size() > 1) {
    		mainRoute.routeLatLng.remove(mainRoute.routeLatLng.size() - 1);
    		mainRoute.routeLatLng.add(endLatLng);
    	}
    	
        return mainRoute;
    }
	
	void moveParser(XmlPullParser parser, int cnt) throws XmlPullParserException, IOException {
		for (int i = 0; i < cnt; i++) {
			parser.next();
		}
	}
	
	public ArrayList<Member> getMemberData() {
    	ArrayList<Member> list = new ArrayList<Member>();
    	
    	if (httpData == null) {
            return list;
        }
    	
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(httpData));
            int eventType = parser.getEventType();
            Member member = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                	break;
                case XmlPullParser.START_TAG:
                	if ("member".equals(parser.getName())) {
                		member = new Member();
                	} else if ("regi_id".equals(parser.getName())) {
                        parser.next();
                        member.regiId = parser.getText();
                    } else if ("my_id".equals(parser.getName())) {
                        parser.next();
                        member.myId = parser.getText();
                    } else if ("name".equals(parser.getName())) {
                        parser.next();
                        member.name = parser.getText();
                    } else if ("image".equals(parser.getName())) {
                        parser.next();
                        member.imageFileName = parser.getText();
                    } 
                    break;
                case XmlPullParser.END_TAG:
                	if ("member".equals(parser.getName())) {
                		list.add(member);
                    }
                    break;
                default:   
                	break;
                }
                eventType = parser.next();
            }
            
        } catch (IllegalStateException e) {
        	Log.e(TAG, e.toString());
        } catch (IOException e) {
        	Log.e(TAG, e.toString());
        } catch (XmlPullParserException e) {
        	Log.e(TAG, e.toString());
        }
    	
        return list;
    }
	
	public int getMemberVersion() {
    	int result = -1;
    	
    	if (httpData == null) {
            return result;
        }
    	
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(httpData));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                	break;
                case XmlPullParser.START_TAG:
                	if ("result".equals(parser.getName())) {
                	} else if ("ver".equals(parser.getName())) {
                        parser.next();
                    } else if ("member_ver".equals(parser.getName())) {
                        parser.next();
                        result = Integer.parseInt(parser.getText());
                    } 
                    break;
                case XmlPullParser.END_TAG:
                	if ("member".equals(parser.getName())) {
                		
                    }
                    break;
                default:   
                	break;
                }
                eventType = parser.next();
            }
            
        } catch (IllegalStateException e) {
        	Log.e(TAG, e.toString());
        } catch (IOException e) {
        	Log.e(TAG, e.toString());
        } catch (XmlPullParserException e) {
        	Log.e(TAG, e.toString());
        }
    	
        return result;
    }
	
	public boolean getResult() {
    	boolean result = false;
    	
    	if (httpData == null) {
            return result;
        }
    	
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(httpData));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
            	if ("result".equals(parser.getName())) {
            		parser.next();
                    String resStr = parser.getText();
                    if (resStr.equals("success")) {
                       	result = true;
                    } else if (resStr.equals("failure")) {
                        result = false;
                    }
                }
                eventType = parser.next();
            }
        } catch (IllegalStateException e) {
        	Log.e(TAG, e.toString());
        } catch (IOException e) {
        	Log.e(TAG, e.toString());
        } catch (XmlPullParserException e) {
        	Log.e(TAG, e.toString());
        }
    	
        return result;
    }	

}
