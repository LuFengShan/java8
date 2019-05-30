package com.java8.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 离线超一个月停放地点在一公里范围内车辆
 *
 * @Author sgx
 * @Date 2019/5/28 13:23
 * @Version
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfflineVehicle {
	/** VIN */
	private String vin;
	/** 车牌 */
	private String licensePlate;
	/** 车辆类别	*/
	private String typeName;
	/** 最后上线时间 */
	private String endOnlineTime;
	/** 离线时长 */
	private int offlineDays;
	/** 经度 */
	private String longitude;
	/** 纬度 */
	private String latitude;
	/** 范围编号	*/
	private int RangeNumber;
	/** 车辆型号 */
	private String vehModelName;
	/** 车辆生产企业 */
	private String unName;
	/** 车辆运营单位 */
	private String unitName;
	/** 仪表里程	*/
	private int meterMileage;
	/** 核算里程	*/
	private int accountingMileage;
	/** SOC */
	private double soc;
}
