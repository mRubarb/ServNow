


cd "C:\LichPublic\BoneYard\SN\AutomationWorkSpace\ServiceNow\"
dir > "C:\LichPublic\BoneYard\SN\AutomationWorkSpace\ServiceNow\PathAtStart.txt"
set JarPath=C:\Users\Bob.lichtenfels\.eclipse\org.eclipse.platform_4.5.0_1681229919_win32_win32_x86_64\plugins\org.testng.eclipse_6.9.5.201508210528\lib\testng.jar
set SelPath=C:\LichPublic\BoneYard\SN\AutomationWorkSpace\ServiceNow\Libs\*
set SelPathJar=C:\LichPublic\BoneYard\SN\AutomationWorkSpace\ServiceNow\libs\selenium-java-2.53.0.jar
set SelBin=C:\LichPublic\BoneYard\SN\AutomationWorkSpace\ServiceNow\bin
echo %JarPath%;%SelPath%;%SelPathJar%;%SelBin% > "C:\LichPublic\BoneYard\SN\AutomationWorkSpace\ServiceNow\ClassPath.txt"
dir > "C:\LichPublic\BoneYard\SN\AutomationWorkSpace\ServiceNow\Done.txt"
set classpath=%JarPath%;%SelPath%;%SelPathJar%;%SelBin%
echo %classpath% > classPath.txt
java org.testng.TestNG C:\LichPublic\BoneYard\SN\AutomationWorkSpace\ServiceNow\TC0016Home.xml  > C:\LichPublic\TC0016Home.txt
java org.testng.TestNG C:\LichPublic\BoneYard\SN\AutomationWorkSpace\ServiceNow\TC0017Home.xml  > C:\LichPublic\TC0017Home.txt







