package DCMSApp;


/**
* DCMSApp/DCMSHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from E:/Eclipse/workspace/COMP6231_Project/src/DCMS.idl
* Friday, July 28, 2017 10:51:04 o'clock AM EDT
*/

abstract public class DCMSHelper
{
  private static String  _id = "IDL:DCMSApp/DCMS:1.0";

  public static void insert (org.omg.CORBA.Any a, DCMSApp.DCMS that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static DCMSApp.DCMS extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (DCMSApp.DCMSHelper.id (), "DCMS");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static DCMSApp.DCMS read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_DCMSStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, DCMSApp.DCMS value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static DCMSApp.DCMS narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof DCMSApp.DCMS)
      return (DCMSApp.DCMS)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      DCMSApp._DCMSStub stub = new DCMSApp._DCMSStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static DCMSApp.DCMS unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof DCMSApp.DCMS)
      return (DCMSApp.DCMS)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      DCMSApp._DCMSStub stub = new DCMSApp._DCMSStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
