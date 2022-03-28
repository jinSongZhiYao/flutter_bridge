class L {
  static bool toggle = false;

  static void log(String msg) {
    if (toggle) {
      print("flutterBridge: $msg");
    }
  }
}
