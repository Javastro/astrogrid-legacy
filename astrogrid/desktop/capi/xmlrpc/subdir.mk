################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
./xmlrpc/XmlRpcClient.cpp \
./xmlrpc/XmlRpcDispatch.cpp \
./xmlrpc/XmlRpcServer.cpp \
./xmlrpc/XmlRpcServerConnection.cpp \
./xmlrpc/XmlRpcServerMethod.cpp \
./xmlrpc/XmlRpcSocket.cpp \
./xmlrpc/XmlRpcSource.cpp \
./xmlrpc/XmlRpcUtil.cpp \
./xmlrpc/XmlRpcValue.cpp 

OBJS += \
./xmlrpc/XmlRpcClient.o \
./xmlrpc/XmlRpcDispatch.o \
./xmlrpc/XmlRpcServer.o \
./xmlrpc/XmlRpcServerConnection.o \
./xmlrpc/XmlRpcServerMethod.o \
./xmlrpc/XmlRpcSocket.o \
./xmlrpc/XmlRpcSource.o \
./xmlrpc/XmlRpcUtil.o \
./xmlrpc/XmlRpcValue.o 

CPP_DEPS += \
./xmlrpc/XmlRpcClient.d \
./xmlrpc/XmlRpcDispatch.d \
./xmlrpc/XmlRpcServer.d \
./xmlrpc/XmlRpcServerConnection.d \
./xmlrpc/XmlRpcServerMethod.d \
./xmlrpc/XmlRpcSocket.d \
./xmlrpc/XmlRpcSource.d \
./xmlrpc/XmlRpcUtil.d \
./xmlrpc/XmlRpcValue.d 


# Each subdirectory must supply rules for building sources it contributes
xmlrpc/%.o: ./xmlrpc/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I"./core" -I"./xmlrpc"  -I/Applications/itt/idl64/external/include -O0 -g3 -Wall -c -fmessage-length=0 -dynamic -v -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o"$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


