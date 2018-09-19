package in.arula.myorder.networkutil;

public class AppConstants {

	public static final String App_UserId = "0";
	public static final String App_GroupID = "0";
	public static final String App_Result = "0";

	// JSON Node names
		public static final String TAG_CONTACTS = "contacts";
		public static final String TAG_ID = "id";
		public static final String TAG_NAME = "name";
		public static final String TAG_EMAIL = "email";
		public static final String TAG_ADDRESS = "address";
		public static final String TAG_GENDER = "gender";
		public static final String TAG_PHONE = "phone";
		public static final String TAG_PHONE_MOBILE = "mobile";
		public static final String TAG_PHONE_HOME = "home";
		public static final String TAG_PHONE_OFFICE = "office";


		// GetAllPOItemDetails
		public static final String TAG_PONUM = "PoNum";
		public static final String TAG_SUPPREF = "SuppRef";
		public static final String TAG_QTYORDERED = "QtyOrdered";
		public static final String TAG_QTYRECEIVED = "QtyReceived";
		public static final String TAG_QTYRECD = "QtyRecd";
		public static final String TAG_QTYPLACED = "QtyPlaced";
		public static final String TAG_STAGINGSTATUS = "StagingStatus";
		public static final String TAG_PLACINGSTATUS = "Placingstatus";

		public static final String TAG_SUMQTYORDERED = "SumQtyOrederd";
		public static final String TAG_SUMQTYRECEIVED = "SumQtyReceived";


		//Common Details
		public static final String TAG_SKU = "SKU";
		public static final String TAG_ITEMNAME = "ItemName";
		public static final String TAG_RECORDID = "RecordID";

		//GetAllPickItemDetails
		public static final String TAG_PICKNUM = "PickNum";
		public static final String TAG_QTYTOPICK = "QtyToPick";
		public static final String TAG_QTYTOPICKed = "QtyPicked";
		public static final String TAG_PICKINGSTATUS = "Pickingstatus";

		public static final String TAG_SUMQTYTOPICK = "SumQtyToPick";
		public static final String TAG_SUMQTYTOPICKED = "SumQtyPicked";
		public static final String TAG_PRNO = "PRNo";
		public static final String TAG_WONUMBER = "Wonumber";


		//GetAllPRItemDetails
		public static final String TAG_PRNUM = "PRNo";
		public static final String TAG_ORDERQTY = "ORDERQty";
		public static final String TAG_ItemID = "ItemID";
		public static final String TAG_ItemName = "ItemName";
		public static final String TAG_ItemDescription = "ItemDescription";
		//GetAndriodPlacingDetailsByItemID
		public static final String TAG_UnitID = "UnitID";
		public static final String TAG_UnitName = "UnitName";
		public static final String TAG_UnitTypeID = "UnitTypeID";
		public static final String TAG_UnitType = "UnitType";
		public static final String TAG_EventID = "EventID";
		public static final String TAG_ParentID = "ParentID";
		public static final String TAG_PropertyID = "PropertyID";
		public static final String TAG_PropertyName = "PropertyName";
		public static final String TAG_Value = "Value";
		public static final String TAG_TotalQtyReceived = "TotalQtyReceived";
		public static final String TAG_TotalQtyAssigned = "TotalQtyAssigned";

		public static final String WAREHOUSE_TYPEID = "9";
		public static final String QTY = "received";

		public static final String UnitType_WAREHOUSE = "3";
		public static final String UnitType_WAREHOUSE_ITRO = "7";
		public static final String UnitType_ROOM = "4";
		public static final String UnitType_RACK = "6";
		public static final String UnitType_SHELF = "7";
		public static final String UnitType_FACILITY = "2";
		public static final String UnitType_BIN = "8";
		public static final String PropertyID_ON = "15"+"_"+AppConstants.TAG_PropertyID;
		public static final String PropertyID_BINCAPCITY = "14"+"_"+AppConstants.TAG_PropertyID;
		public static final String PropertyID_PAR = "27"+"_"+AppConstants.TAG_PropertyID;
		public static final String PropertyID_CRITICAl = "31"+"_"+AppConstants.TAG_PropertyID;
		public static final String PropertyID_Item = "2"+"_"+AppConstants.TAG_PropertyID;
		public static final String PropertyID_SKU = "1"+"_"+AppConstants.TAG_PropertyID;


		public static final String UnitTypeID_WAREHOUSE_O = "9";
		public static final String UnitType_RACK_O = "9";
		public static final String UnitType_SHELF_O = "53";
		public static final String UnitType_FACILITY_O = "2";
		public static final String UnitType_BIN_O = "11";
		public static final String PropertyID_ON_O = "15"+"_"+AppConstants.TAG_PropertyID;
		public static final String PropertyID_Item_O = "2"+"_"+AppConstants.TAG_PropertyID;
		public static final String PropertyID_SKU_O = "1"+"_"+AppConstants.TAG_PropertyID;

		//GetAndriodItemMultipleLocations
		public static final String TAG_BinID = "BinID";
		public static final String TAG_BinName = "BinName";
//		public static final String TAG_warehouseid = "warehouseid";
		public static final String TAG_Warehouse = "Warehouse";
		public static final String TAG_warehousetypeid = "WarehouseTypeID";
		public static final String TAG_capacity = "capacity";
		public static final String TAG_OnHand = "OnHand";
		public static final String TAG_SpaceQty = "SpaceQty";
		public static final String TAG_PARLevel = "PARLevel";
		public static final String TAG_CriticalLevel = "CriticalLevel";
		public static final String TAG_AssignedQty = "AssignQty";
		public static final String TAG_AssignedQty2 = "AssignedQty";
		public static final String TAG_Mcount = "Mcount";
		public static final String TAG_RemainingQty = "RemainingQty";
		public static final String TAG_RoomName = "RoomName";

		public static final String TAG_TAG_PropertyName_Item = "Item Name";


		public static final String TAG_TRQty = "TR_Qty";
		public static final String TAG_PRQty = "PR_Qty";

		public static final String TAG_TRNO = "TRNo";


		public static final String TAG_Result = "Result";


		//UserDetails
		 public static final String TAG_UserID = "UserID";
		 public static final String TAG_GroupID = "GroupID";
		 public static final String TAG_LoginID = "LoginID";
		 public static final String TAG_FirstName = "FirstName";
		 public static final String TAG_LastName = "LastName";
		 public static final String TAG_EmailId = "EmailId";
		 public static final String TAG_Password = "Password";
		 public static final String TAG_PhoneNumber = "PhoneNumber";
		 public static final String TAG_Active = "Active";
		 public static final String TAG_Address = "Address";
		 public static final String TAG_UserGroup = "UserGroup";
		 public static final String TAG_Name = "Name";
}
